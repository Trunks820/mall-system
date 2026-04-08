package com.kakarot.mall.product.service.impl;

import com.kakarot.mall.common.exception.BizException;
import com.kakarot.mall.common.page.PageResult;
import com.kakarot.mall.product.entity.Product;
import com.kakarot.mall.product.mapper.ProductMapper;
import com.kakarot.mall.product.mapper.ProductSkuMapper;
import com.kakarot.mall.product.query.ProductListQuery;
import com.kakarot.mall.product.query.ProductSearchQuery;
import com.kakarot.mall.product.service.ProductService;
import com.kakarot.mall.product.vo.ProductDetailVO;
import com.kakarot.mall.product.vo.ProductListVO;
import com.kakarot.mall.product.vo.SkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;

    @Override
    public PageResult<ProductListVO> listProducts(ProductListQuery query) {
        long total = productMapper.countProductList(query);
        if (total == 0) {
            return PageResult.empty(query);
        }
        List<ProductListVO> list = productMapper.selectProductList(query);
        return PageResult.of(query, total, list);
    }

    @Override
    public ProductDetailVO getProductDetail(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BizException("商品不存在或已下架");
        }

        List<SkuVO> skuList = productSkuMapper.selectByProductId(productId);

        ProductDetailVO vo = new ProductDetailVO();
        vo.setId(product.getId());
        vo.setCategoryId(product.getCategoryId());
        vo.setProductName(product.getProductName());
        vo.setProductDesc(product.getProductDesc());
        vo.setMainImage(product.getMainImage());
        vo.setBannerImages(product.getBannerImages());
        vo.setSkuList(skuList);
        return vo;
    }

    @Override
    public PageResult<ProductListVO> searchProducts(ProductSearchQuery query) {
        long total = productMapper.countSearchProductList(query);
        if (total == 0) {
            return PageResult.empty(query);
        }
        List<ProductListVO> list = productMapper.searchProductList(query);
        return PageResult.of(query, total, list);
    }
}
