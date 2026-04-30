package com.gdshop.controller;

import com.gdshop.dto.Result;
import com.gdshop.entity.ShopConfig;
import com.gdshop.service.IShopConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/config")
public class ShopConfigController {

    @Resource
    private IShopConfigService shopConfigService;

    @GetMapping
    public Result getConfig() {
        return shopConfigService.getConfig();
    }

    @PutMapping
    public Result saveConfig(@RequestBody ShopConfig config) {
        return shopConfigService.saveConfig(config);
    }
}
