package com.kakarot.mall.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductDetailVO implements Serializable {

    private Long id;

    private Long categoryId;

    private String productName;

    private String productDesc;

    private String mainImage;

    private String bannerImages;

    private List<SkuVO> skuList;
}
