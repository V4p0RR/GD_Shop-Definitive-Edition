package com.gdshop.controller;

import com.gdshop.dto.Result;
import com.gdshop.entity.SeckillActivity;
import com.gdshop.service.ISeckillService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Resource
    private ISeckillService seckillService;

    @PostMapping("/activity")
    public Result createActivity(@RequestBody SeckillActivity activity) {
        return seckillService.createActivity(activity);
    }

    @GetMapping("/list")
    public Result queryActivityList() {
        return seckillService.queryActivityList();
    }

    @GetMapping("/activity/{id}")
    public Result queryActivityById(@PathVariable Long id) {
        return seckillService.queryActivityById(id);
    }

    @PostMapping("/order/{activityId}")
    public Result seckill(@PathVariable Long activityId) {
        return seckillService.seckill(activityId);
    }

    @DeleteMapping("/activity/{id}")
    public Result deleteActivity(@PathVariable Long id) {
        return seckillService.deleteActivity(id);
    }
}
