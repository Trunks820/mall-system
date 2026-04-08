package com.kakarot.mall.product.query;

import com.kakarot.mall.common.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductListQuery extends PageQuery {

    private Long categoryId;
}
