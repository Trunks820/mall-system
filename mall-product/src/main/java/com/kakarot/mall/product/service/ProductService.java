package com.kakarot.mall.product.service;

import com.kakarot.mall.common.page.PageResult;
import com.kakarot.mall.product.query.ProductListQuery;
import com.kakarot.mall.product.query.ProductSearchQuery;
import com.kakarot.mall.product.vo.ProductDetailVO;
import com.kakarot.mall.product.vo.ProductListVO;

public interface ProductService {

    PageResult<ProductListVO> listProducts(ProductListQuery query);

    ProductDetailVO getProductDetail(Long productId);

    PageResult<ProductListVO> searchProducts(ProductSearchQuery query);
}
