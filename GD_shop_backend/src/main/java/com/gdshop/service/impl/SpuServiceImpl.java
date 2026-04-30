package com.gdshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdshop.dto.Result;
import com.gdshop.entity.Spu;
import com.gdshop.mapper.SpuMapper;
import com.gdshop.service.ISpuService;
import com.gdshop.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements ISpuService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result querySpuById(Long id) {
        // 先查Redis缓存
        String key = RedisConstants.CACHE_SPU_KEY + id;
        String spuJson = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(spuJson)) {
            Spu spu = JSONUtil.toBean(spuJson, Spu.class);
            return Result.ok(spu);
        }

        Spu spu = getById(id);
        if (spu == null) {
            return Result.fail("商品不存在");
        }
        // 写缓存
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(spu),
                RedisConstants.CACHE_SPU_TTL, TimeUnit.MINUTES);
        return Result.ok(spu);
    }

    @Override
    public Result querySpuList(Integer current, Long categoryId, String keyword) {
        Page<Spu> page = new Page<>(current, 10);
        LambdaQueryWrapper<Spu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Spu::getStatus, 1);
        if (categoryId != null) {
            wrapper.eq(Spu::getCategoryId, categoryId);
        }
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Spu::getName, keyword);
        }
        wrapper.orderByDesc(Spu::getCreateTime);
        page(page, wrapper);
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @Override
    public Result createSpu(Spu spu) {
        save(spu);
        return Result.ok(spu.getId());
    }

    @Override
    public Result updateSpu(Spu spu) {
        updateById(spu);
        // 删除缓存
        stringRedisTemplate.delete(RedisConstants.CACHE_SPU_KEY + spu.getId());
        return Result.ok();
    }

    @Override
    public Result deleteSpu(Long id) {
        removeById(id);
        stringRedisTemplate.delete(RedisConstants.CACHE_SPU_KEY + id);
        return Result.ok();
    }

    @Override
    public Result adminSpuList(Integer current, Long categoryId, String keyword) {
        Page<Spu> page = new Page<>(current, 10);
        LambdaQueryWrapper<Spu> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(Spu::getCategoryId, categoryId);
        }
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Spu::getName, keyword);
        }
        wrapper.orderByDesc(Spu::getCreateTime);
        page(page, wrapper);
        return Result.ok(page.getRecords(), page.getTotal());
    }
}
