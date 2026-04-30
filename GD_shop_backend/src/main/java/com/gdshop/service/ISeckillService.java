package com.gdshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdshop.dto.Result;
import com.gdshop.entity.SeckillActivity;

public interface ISeckillService extends IService<SeckillActivity> {
    Result createActivity(SeckillActivity activity);
    Result queryActivityList();
    Result queryActivityById(Long id);
    Result seckill(Long activityId);
    Result deleteActivity(Long id);
}
