package com.kakarot.mall.cart.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SettlementItemVO implements Serializable {

    private Long skuId;

    private Long productId;

    private String productName;

    private String skuName;

    private String mainImage;

    private BigDecimal salePrice;

    private Integer quantity;

    /** salePrice * quantity */
    private BigDecimal lineTotal;
}
