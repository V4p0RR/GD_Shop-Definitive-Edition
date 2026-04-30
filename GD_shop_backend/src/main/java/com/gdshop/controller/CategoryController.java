package com.gdshop.controller;

import com.gdshop.dto.Result;
import com.gdshop.service.ICategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    @GetMapping("/list")
    public Result list() {
        return categoryService.listAll();
    }
}
