package com.gdshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdshop.dto.Result;
import com.gdshop.entity.Category;
import com.gdshop.mapper.CategoryMapper;
import com.gdshop.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public Result listAll() {
        List<Category> list = list(new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort));
        return Result.ok(list);
    }
}
