package com.kakarot.mall.product.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {

    private Long id;

    private Long categoryId;

    private String productName;

    private String productDesc;

    private String mainImage;

    private String bannerImages;

    private Integer status;

    private Integer sortNum;

    private String extJson;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
