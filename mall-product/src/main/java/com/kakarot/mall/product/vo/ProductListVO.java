package com.kakarot.mall.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductListVO implements Serializable {

    private Long id;

    private Long categoryId;

    private String productName;

    private String productDesc;

    private String mainImage;

    /** 该商品下最低 SKU 售价 */
    private BigDecimal salePrice;
}
