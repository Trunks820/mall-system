package com.kakarot.mall.product.controller;

import com.kakarot.mall.common.result.Result;
import com.kakarot.mall.product.service.CategoryService;
import com.kakarot.mall.product.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/app/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<CategoryVO>> list() {
        return Result.success(categoryService.listEnabled());
    }
}
