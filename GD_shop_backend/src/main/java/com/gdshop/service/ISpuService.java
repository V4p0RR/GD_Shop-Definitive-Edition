package com.gdshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdshop.dto.Result;
import com.gdshop.entity.Spu;

public interface ISpuService extends IService<Spu> {
    Result querySpuById(Long id);
    Result querySpuList(Integer current, Long categoryId, String keyword);
    Result createSpu(Spu spu);
    Result updateSpu(Spu spu);
    Result deleteSpu(Long id);
    Result adminSpuList(Integer current, Long categoryId, String keyword);
}
