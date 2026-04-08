package com.kakarot.mall.product.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {

    private Long id;

    private String categoryName;

    private Integer sortNum;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
