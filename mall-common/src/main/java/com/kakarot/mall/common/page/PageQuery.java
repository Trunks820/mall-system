package com.kakarot.mall.common.page;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageQuery implements Serializable {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
