package com.gdshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdshop.dto.Result;
import com.gdshop.entity.Order;
import com.gdshop.entity.OrderItem;
import com.gdshop.entity.Sku;
import com.gdshop.mapper.OrderItemMapper;
import com.gdshop.mapper.OrderMapper;
import com.gdshop.service.IOrderService;
import com.gdshop.service.ISkuService;
import com.gdshop.utils.RedisIdWorker;
import com.gdshop.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private ISkuService skuService;
    @Resource
    private RedisIdWorker redisIdWorker;

    @Override
    @Transactional
    public Result createOrder(Order order, List<Long> skuIds, List<Integer> quantities) {
        // 先查询所有SKU并校验，避免中途失败导致部分库存已扣减
        List<Sku> skuList = new ArrayList<>();
        for (int i = 0; i < skuIds.size(); i++) {
            Sku sku = skuService.getById(skuIds.get(i));
            if (sku == null || sku.getStatus() == 0) {
                return Result.fail("商品 " + skuIds.get(i) + " 已下架");
            }
            if (sku.getStock() < quantities.get(i)) {
                return Result.fail("商品 " + sku.getName() + " 库存不足");
            }
            skuList.add(sku);
        }

        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("order");
        order.setId(orderId);
        order.setUserId(userId);
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        long totalAmount = 0;
        List<OrderItem> items = new ArrayList<>();

        for (int i = 0; i < skuList.size(); i++) {
            Sku sku = skuList.get(i);
            int qty = quantities.get(i);
            sku.setStock(sku.getStock() - qty);
            skuService.updateById(sku);

            long itemTotal = sku.getPrice() * qty;
            totalAmount += itemTotal;

            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setSkuId(sku.getId());
            item.setSpuId(sku.getSpuId());
            item.setName(sku.getName());
            item.setSpecs(sku.getSpecs());
            item.setPrice(sku.getPrice());
            item.setQuantity(qty);
            item.setImage(sku.getImage());
            items.add(item);
        }

        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        save(order);

        for (OrderItem item : items) {
            orderItemMapper.insert(item);
        }
        return Result.ok(orderId);
    }

    @Override
    public Result queryOrderList(Integer current, Integer status) {
        Long userId = UserHolder.getUser().getId();
        Page<Order> page = new Page<>(current, 10);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        page(page, wrapper);
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @Override
    public Result queryOrderDetail(Long id) {
        Order order = getById(id);
        if (order == null) {
            return Result.fail("订单不存在");
        }
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id));
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return Result.ok(result);
    }

    @Override
    public Result shipOrder(Long id) {
        Order order = getById(id);
        if (order == null) {
            return Result.fail("订单不存在");
        }
        if (order.getStatus() != 1) {
            return Result.fail("订单状态不正确，仅已支付订单可发货");
        }
        order.setStatus(2);
        order.setShipTime(LocalDateTime.now());
        updateById(order);
        return Result.ok();
    }

    @Override
    @Transactional
    public Result refundOrder(Long id) {
        Order order = getById(id);
        if (order == null) {
            return Result.fail("订单不存在");
        }
        if (order.getStatus() == 5 || order.getStatus() == 6) {
            return Result.fail("订单已退款或退款中");
        }
        if (order.getStatus() >= 3) {
            return Result.fail("已完成订单不可退款");
        }
        order.setStatus(6);
        updateById(order);

        // 退还库存
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id));
        for (OrderItem item : items) {
            Sku sku = skuService.getById(item.getSkuId());
            if (sku != null) {
                sku.setStock(sku.getStock() + item.getQuantity());
                skuService.updateById(sku);
            }
        }
        return Result.ok();
    }

    @Override
    public Result adminOrderList(Integer current, Integer status) {
        Page<Order> page = new Page<>(current, 10);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        page(page, wrapper);
        return Result.ok(page.getRecords(), page.getTotal());
    }
}
