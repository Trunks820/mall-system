package com.kakarot.mall.product.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductSku {

    private Long id;

    private Long productId;

    private String skuCode;

    private String skuName;

    private String specJson;

    private BigDecimal salePrice;

    private BigDecimal originPrice;

    private Integer stock;

    private Integer lockStock;

    private Integer saleCount;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
