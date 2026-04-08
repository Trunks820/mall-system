package com.kakarot.mall.common.page;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {

    private Integer pageNum;

    private Integer pageSize;

    private Long total;

    private Integer totalPages;

    private List<T> list;

    public PageResult() {
    }

    public PageResult(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
        this.list = list;
    }

    public static <T> PageResult<T> of(PageQuery query, Long total, List<T> list) {
        return new PageResult<>(query.getPageNum(), query.getPageSize(), total, list);
    }

    public static <T> PageResult<T> empty(PageQuery query) {
        return new PageResult<>(query.getPageNum(), query.getPageSize(), 0L, Collections.emptyList());
    }
}
