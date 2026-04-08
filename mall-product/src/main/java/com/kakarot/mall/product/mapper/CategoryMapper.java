package com.kakarot.mall.product.mapper;

import com.kakarot.mall.product.vo.CategoryVO;

import java.util.List;

public interface CategoryMapper {

    List<CategoryVO> selectEnabledList();
}
