package com.gdshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdshop.dto.Result;
import com.gdshop.entity.Sku;

public interface ISkuService extends IService<Sku> {
    Result querySkuBySpuId(Long spuId);
    Result querySkuById(Long id);
    Result createSku(Sku sku);
    Result updateSku(Sku sku);
    Result deleteSku(Long id);
    Result batchSaveSkus(java.util.List<Sku> skuList);
}
