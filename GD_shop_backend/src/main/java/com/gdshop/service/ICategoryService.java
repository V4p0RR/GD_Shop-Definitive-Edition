package com.gdshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdshop.dto.Result;
import com.gdshop.entity.Category;

public interface ICategoryService extends IService<Category> {
    Result listAll();
}
