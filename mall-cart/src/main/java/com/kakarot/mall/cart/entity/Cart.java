package com.kakarot.mall.cart.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cart {

    private Long id;

    private Long userId;

    private Long productId;

    private Long skuId;

    private Integer quantity;

    private Integer checked;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
