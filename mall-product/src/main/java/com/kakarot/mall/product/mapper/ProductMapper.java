package com.kakarot.mall.product.mapper;

import com.kakarot.mall.product.entity.Product;
import com.kakarot.mall.product.query.ProductListQuery;
import com.kakarot.mall.product.query.ProductSearchQuery;
import com.kakarot.mall.product.vo.ProductListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {

    List<ProductListVO> selectProductList(ProductListQuery query);

    long countProductList(ProductListQuery query);

    Product selectById(@Param("id") Long id);

    List<ProductListVO> searchProductList(ProductSearchQuery query);

    long countSearchProductList(ProductSearchQuery query);
}
