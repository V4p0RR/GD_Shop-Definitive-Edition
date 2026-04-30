package com.gdshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdshop.dto.Result;
import com.gdshop.entity.SeckillActivity;
import com.gdshop.entity.SeckillOrder;
import com.gdshop.mapper.SeckillActivityMapper;
import com.gdshop.mapper.SeckillOrderMapper;
import com.gdshop.service.ISeckillService;
import com.gdshop.utils.RedisConstants;
import com.gdshop.utils.RedisIdWorker;
import com.gdshop.utils.UserHolder;

import cn.hutool.core.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class SeckillServiceImpl extends ServiceImpl<SeckillActivityMapper, SeckillActivity> implements ISeckillService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    private static final ExecutorService SECKILL_EXECUTOR = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init() {
        SECKILL_EXECUTOR.submit(new SeckillOrderHandler());
    }

    private class SeckillOrderHandler implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    // 从队列中获取订单信息
                    String orderInfo = stringRedisTemplate.opsForList()
                            .rightPop("seckill:order:queue", 5, java.util.concurrent.TimeUnit.SECONDS);
                    if (orderInfo == null) {
                        continue;
                    }
                    // 格式: activityId:userId:orderId
                    String[] parts = orderInfo.split(":");
                    if (parts.length < 3)
                        continue;
                    Long activityId = Long.valueOf(parts[0]);
                    Long userId = Long.valueOf(parts[1]);
                    Long orderId = Long.valueOf(parts[2]);
                    handleSeckillOrder(activityId, userId, orderId);
                } catch (Exception e) {
                    log.error("处理秒杀订单异常", e);
                }
            }
        }
    }

    private void handleSeckillOrder(Long activityId, Long userId, Long orderId) {
        RLock lock = redissonClient.getLock("lock:seckill:order:" + userId + ":" + activityId);
        boolean isLock = lock.tryLock();
        if (!isLock) {
            log.error("重复下单 userId={} activityId={}", userId, activityId);
            return;
        }
        try {
            // 检查是否已下单
            int count = seckillOrderMapper.selectCount(
                    new LambdaQueryWrapper<SeckillOrder>()
                            .eq(SeckillOrder::getUserId, userId)
                            .eq(SeckillOrder::getActivityId, activityId));
            if (count > 0) {
                log.error("已参与过秒杀 userId={} activityId={}", userId, activityId);
                return;
            }
            // 扣减数据库库存
            SeckillActivity activity = getById(activityId);
            boolean success = update()
                    .setSql("stock = stock - 1")
                    .eq("id", activityId).gt("stock", 0)
                    .update();
            if (!success) {
                log.error("库存不足 activityId={}", activityId);
                return;
            }
            // 创建订单
            SeckillOrder order = new SeckillOrder();
            order.setId(orderId);
            order.setUserId(userId);
            order.setActivityId(activityId);
            order.setSkuId(activity.getSkuId());
            order.setSeckillPrice(activity.getSeckillPrice());
            order.setStatus(1);
            order.setCreateTime(LocalDateTime.now());
            seckillOrderMapper.insert(order);
            log.info("秒杀订单创建成功 orderId={}", orderId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public Result createActivity(SeckillActivity activity) {
        save(activity);
        // 预加载库存到Redis
        stringRedisTemplate.opsForValue().set(
                RedisConstants.SECKILL_STOCK_KEY + activity.getId(),
                activity.getStock().toString());
        return Result.ok(activity.getId());
    }

    @Override
    public Result queryActivityList() {
        List<SeckillActivity> list = list(new LambdaQueryWrapper<SeckillActivity>()
                .orderByDesc(SeckillActivity::getCreateTime));
        return Result.ok(list);
    }

    @Override
    public Result queryActivityById(Long id) {
        SeckillActivity activity = getById(id);
        if (activity == null) {
            return Result.fail("秒杀活动不存在");
        }
        return Result.ok(activity);
    }

    @Override
    public Result seckill(Long activityId) {
        SeckillActivity activity = getById(activityId);
        if (activity == null) {
            return Result.fail("秒杀活动不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getBeginTime())) {
            return Result.fail("秒杀未开始");
        }
        if (now.isAfter(activity.getEndTime())) {
            return Result.fail("秒杀已结束");
        }

        // 确保库存已加载到Redis
        String stockKey = RedisConstants.SECKILL_STOCK_KEY + activityId;
        if (BooleanUtil.isFalse(stringRedisTemplate.hasKey(stockKey))) {
            stringRedisTemplate.opsForValue().set(stockKey, activity.getStock().toString());
        }

        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("seckill");

        // 执行Lua脚本
        Long result = stringRedisTemplate.execute(SECKILL_SCRIPT,
                Collections.emptyList(),
                activityId.toString(),
                userId.toString(),
                String.valueOf(orderId));
        int r = result.intValue();
        if (r != 0) {
            return Result.fail(r == 1 ? "库存不足" : "一人一单，请勿重复下单！");
        }
        return Result.ok(orderId);
    }

    @Override
    public Result deleteActivity(Long id) {
        removeById(id);
        stringRedisTemplate.delete(RedisConstants.SECKILL_STOCK_KEY + id);
        stringRedisTemplate.delete(RedisConstants.SECKILL_ORDER_KEY + id);
        return Result.ok();
    }
}
