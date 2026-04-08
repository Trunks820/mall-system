package com.kakarot.mall.product.controller;

import com.kakarot.mall.common.page.PageResult;
import com.kakarot.mall.common.result.Result;
import com.kakarot.mall.product.query.ProductListQuery;
import com.kakarot.mall.product.query.ProductSearchQuery;
import com.kakarot.mall.product.service.ProductService;
import com.kakarot.mall.product.vo.ProductDetailVO;
import com.kakarot.mall.product.vo.ProductListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public Result<PageResult<ProductListVO>> list(ProductListQuery query) {
        return Result.success(productService.listProducts(query));
    }

    @GetMapping("/detail")
    public Result<ProductDetailVO> detail(@RequestParam Long productId) {
        return Result.success(productService.getProductDetail(productId));
    }

    @GetMapping("/search")
    public Result<PageResult<ProductListVO>> search(ProductSearchQuery query) {
        return Result.success(productService.searchProducts(query));
    }
}
