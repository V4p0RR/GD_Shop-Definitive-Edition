package com.gdshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdshop.dto.Result;
import com.gdshop.entity.OperationLog;

public interface IOperationLogService extends IService<OperationLog> {
    Result listLogs(Integer current, String keyword, String type, String beginDate, String endDate);
}
