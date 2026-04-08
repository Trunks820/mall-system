package com.kakarot.mall.product.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryVO implements Serializable {

    private Long id;

    private String categoryName;

    private Integer sortNum;
}
