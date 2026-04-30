package com.gdshop.controller;

import com.gdshop.dto.Result;
import com.gdshop.entity.Sku;
import com.gdshop.service.ISkuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuController {

    @Resource
    private ISkuService skuService;

    @GetMapping("/list/{spuId}")
    public Result querySkuBySpuId(@PathVariable Long spuId) {
        return skuService.querySkuBySpuId(spuId);
    }

    @GetMapping("/{id}")
    public Result querySkuById(@PathVariable Long id) {
        return skuService.querySkuById(id);
    }

    @PostMapping
    public Result createSku(@RequestBody Sku sku) {
        return skuService.createSku(sku);
    }

    @PostMapping("/batch")
    public Result batchSaveSkus(@RequestBody List<Sku> skuList) {
        return skuService.batchSaveSkus(skuList);
    }

    @PutMapping
    public Result updateSku(@RequestBody Sku sku) {
        return skuService.updateSku(sku);
    }

    @DeleteMapping("/{id}")
    public Result deleteSku(@PathVariable Long id) {
        return skuService.deleteSku(id);
    }
}
