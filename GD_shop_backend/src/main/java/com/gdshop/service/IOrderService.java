package com.gdshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdshop.dto.Result;
import com.gdshop.entity.Order;

import java.util.List;

public interface IOrderService extends IService<Order> {
    Result createOrder(Order order, List<Long> skuIds, List<Integer> quantities);
    Result queryOrderList(Integer current, Integer status);
    Result queryOrderDetail(Long id);
    Result shipOrder(Long id);
    Result refundOrder(Long id);
    Result adminOrderList(Integer current, Integer status);
}
