package com.kakarot.mall.cart.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartItemVO implements Serializable {

    private Long id;

    private Long productId;

    private Long skuId;

    private String productName;

    private String mainImage;

    private String skuName;

    private BigDecimal salePrice;

    private Integer quantity;

    private Integer checked;

    /** 可用库存 = stock - lock_stock */
    private Integer availableStock;

    /** 商品状态：1-上架 0-下架 */
    private Integer productStatus;

    /** SKU 状态：1-启用 0-禁用 */
    private Integer skuStatus;
}
