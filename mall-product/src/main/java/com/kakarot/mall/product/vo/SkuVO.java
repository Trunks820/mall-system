package com.kakarot.mall.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SkuVO implements Serializable {

    private Long id;

    private String skuCode;

    private String skuName;

    private BigDecimal salePrice;

    private BigDecimal originPrice;

    /** 可用库存 = stock - lockStock */
    private Integer availableStock;
}
