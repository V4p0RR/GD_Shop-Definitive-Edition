package com.gdshop.controller;

import com.gdshop.dto.Result;
import com.gdshop.service.IOperationLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/log")
public class OperationLogController {

    @Resource
    private IOperationLogService operationLogService;

    @GetMapping("/admin/list")
    public Result listLogs(@RequestParam(defaultValue = "1") Integer current,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String type,
                           @RequestParam(required = false) String beginDate,
                           @RequestParam(required = false) String endDate) {
        return operationLogService.listLogs(current, keyword, type, beginDate, endDate);
    }
}
