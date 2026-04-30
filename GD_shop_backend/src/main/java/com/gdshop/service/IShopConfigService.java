package com.gdshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdshop.dto.Result;
import com.gdshop.entity.ShopConfig;

public interface IShopConfigService extends IService<ShopConfig> {
    Result getConfig();
    Result saveConfig(ShopConfig config);
}
