package com.kakarot.mall.product.query;

import com.kakarot.mall.common.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSearchQuery extends PageQuery {

    private String keyword;
}
