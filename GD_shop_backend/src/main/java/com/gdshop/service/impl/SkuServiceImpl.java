package com.gdshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdshop.dto.Result;
import com.gdshop.entity.Sku;
import com.gdshop.mapper.SkuMapper;
import com.gdshop.service.ISkuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

    @Override
    public Result querySkuBySpuId(Long spuId) {
        List<Sku> list = list(new LambdaQueryWrapper<Sku>()
                .eq(Sku::getSpuId, spuId)
                .eq(Sku::getStatus, 1));
        return Result.ok(list);
    }

    @Override
    public Result querySkuById(Long id) {
        Sku sku = getById(id);
        if (sku == null) {
            return Result.fail("SKU不存在");
        }
        return Result.ok(sku);
    }

    @Override
    public Result createSku(Sku sku) {
        save(sku);
        return Result.ok(sku.getId());
    }

    @Override
    public Result updateSku(Sku sku) {
        updateById(sku);
        return Result.ok();
    }

    @Override
    public Result deleteSku(Long id) {
        removeById(id);
        return Result.ok();
    }

    @Override
    public Result batchSaveSkus(List<Sku> skuList) {
        saveBatch(skuList);
        return Result.ok();
    }
}
