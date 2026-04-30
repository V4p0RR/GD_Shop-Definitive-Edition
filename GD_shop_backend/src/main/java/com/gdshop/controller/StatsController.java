package com.gdshop.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gdshop.dto.Result;
import com.gdshop.entity.Order;
import com.gdshop.service.IOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @Resource
    private IOrderService orderService;

    @GetMapping("/daily")
    public Result dailyStats(@RequestParam(required = false) String date) {
        if (date == null || date.isEmpty()) {
            date = LocalDate.now().toString();
        }
        LocalDate targetDate = LocalDate.parse(date);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.atTime(LocalTime.MAX);

        List<Order> orders = orderService.list(new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime, start)
                .le(Order::getCreateTime, end)
                .ne(Order::getStatus, 4));

        long orderCount = orders.size();
        long totalAmount = orders.stream().mapToLong(Order::getPayAmount).sum();

        Map<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("orderCount", orderCount);
        result.put("totalAmount", totalAmount);
        return Result.ok(result);
    }

    @GetMapping("/recent")
    public Result recentStats(@RequestParam(defaultValue = "7") Integer days) {
        List<Map<String, Object>> stats = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate targetDate = LocalDate.now().minusDays(i);
            LocalDateTime start = targetDate.atStartOfDay();
            LocalDateTime end = targetDate.atTime(LocalTime.MAX);

            List<Order> orders = orderService.list(new LambdaQueryWrapper<Order>()
                    .ge(Order::getCreateTime, start)
                    .le(Order::getCreateTime, end)
                    .ne(Order::getStatus, 4));

            Map<String, Object> daily = new HashMap<>();
            daily.put("date", targetDate.toString());
            daily.put("orderCount", orders.size());
            daily.put("totalAmount", orders.stream().mapToLong(Order::getPayAmount).sum());
            stats.add(daily);
        }
        return Result.ok(stats);
    }
}
