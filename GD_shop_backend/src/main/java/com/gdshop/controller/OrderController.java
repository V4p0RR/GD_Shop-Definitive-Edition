package com.gdshop.controller;

import com.gdshop.dto.CreateOrderDTO;
import com.gdshop.dto.Result;
import com.gdshop.entity.Order;
import com.gdshop.service.IOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @PostMapping
    public Result createOrder(@RequestBody CreateOrderDTO dto) {
        Order order = new Order();
        order.setAddress(dto.getAddress());
        order.setPhone(dto.getPhone());
        order.setReceiver(dto.getReceiver());
        order.setRemark(dto.getRemark() != null ? dto.getRemark() : "");
        return orderService.createOrder(order, dto.getSkuIds(), dto.getQuantities());
    }

    @GetMapping("/list")
    public Result queryOrderList(@RequestParam(defaultValue = "1") Integer current,
                                 @RequestParam(required = false) Integer status) {
        return orderService.queryOrderList(current, status);
    }

    @GetMapping("/{id}")
    public Result queryOrderDetail(@PathVariable Long id) {
        return orderService.queryOrderDetail(id);
    }

    @PutMapping("/ship/{id}")
    public Result shipOrder(@PathVariable Long id) {
        return orderService.shipOrder(id);
    }

    @PutMapping("/refund/{id}")
    public Result refundOrder(@PathVariable Long id) {
        return orderService.refundOrder(id);
    }

    @GetMapping("/admin/list")
    public Result adminOrderList(@RequestParam(defaultValue = "1") Integer current,
                                 @RequestParam(required = false) Integer status) {
        return orderService.adminOrderList(current, status);
    }
}
