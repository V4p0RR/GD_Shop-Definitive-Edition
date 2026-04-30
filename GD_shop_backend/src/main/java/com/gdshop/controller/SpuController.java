package com.gdshop.controller;

import com.gdshop.dto.Result;
import com.gdshop.entity.Spu;
import com.gdshop.service.ISpuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/spu")
public class SpuController {

    @Resource
    private ISpuService spuService;

    @GetMapping("/{id}")
    public Result querySpuById(@PathVariable Long id) {
        return spuService.querySpuById(id);
    }

    @GetMapping("/list")
    public Result querySpuList(@RequestParam(defaultValue = "1") Integer current,
                               @RequestParam(required = false) Long categoryId,
                               @RequestParam(required = false) String keyword) {
        return spuService.querySpuList(current, categoryId, keyword);
    }

    @PostMapping
    public Result createSpu(@RequestBody Spu spu) {
        return spuService.createSpu(spu);
    }

    @PutMapping
    public Result updateSpu(@RequestBody Spu spu) {
        return spuService.updateSpu(spu);
    }

    @DeleteMapping("/{id}")
    public Result deleteSpu(@PathVariable Long id) {
        return spuService.deleteSpu(id);
    }

    @GetMapping("/admin/list")
    public Result adminSpuList(@RequestParam(defaultValue = "1") Integer current,
                               @RequestParam(required = false) Long categoryId,
                               @RequestParam(required = false) String keyword) {
        return spuService.adminSpuList(current, categoryId, keyword);
    }
}
