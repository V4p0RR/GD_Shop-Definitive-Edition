package com.gdshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdshop.dto.Result;
import com.gdshop.entity.ShopConfig;
import com.gdshop.mapper.ShopConfigMapper;
import com.gdshop.service.IShopConfigService;

import org.springframework.stereotype.Service;

@Service
public class ShopConfigServiceImpl extends ServiceImpl<ShopConfigMapper, ShopConfig> implements IShopConfigService {

    @Override
    public Result getConfig() {
        ShopConfig config = getById(1);
        if (config == null) {
            config = new ShopConfig();
            config.setId(1L);
            config.setShopName("贵大电商");
            config.setServicePhone("");
            config.setAnnouncement("");
            config.setWechatPay(1);
            config.setAlipay(1);
            save(config);
        }
        return Result.ok(config);
    }

    @Override
    public Result saveConfig(ShopConfig config) {
        config.setId(1L);
        saveOrUpdate(config);
        return Result.ok();
    }
}
