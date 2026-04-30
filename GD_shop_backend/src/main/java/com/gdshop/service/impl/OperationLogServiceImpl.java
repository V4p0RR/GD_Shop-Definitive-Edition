package com.gdshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdshop.dto.Result;
import com.gdshop.entity.OperationLog;
import com.gdshop.mapper.OperationLogMapper;
import com.gdshop.service.IOperationLogService;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

    @Override
    public Result listLogs(Integer current, String keyword, String type, String beginDate, String endDate) {
        Page<OperationLog> page = new Page<>(current, 15);
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(type) && !"all".equals(type)) {
            wrapper.eq(OperationLog::getType, type);
        }
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(OperationLog::getContent, keyword)
                    .or().like(OperationLog::getOperator, keyword));
        }
        if (StrUtil.isNotBlank(beginDate)) {
            LocalDateTime start = LocalDate.parse(beginDate).atStartOfDay();
            wrapper.ge(OperationLog::getCreateTime, start);
        }
        if (StrUtil.isNotBlank(endDate)) {
            LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
            wrapper.le(OperationLog::getCreateTime, end);
        }
        wrapper.orderByDesc(OperationLog::getCreateTime);
        page(page, wrapper);
        return Result.ok(page.getRecords(), page.getTotal());
    }
}
