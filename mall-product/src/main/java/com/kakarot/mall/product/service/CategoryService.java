package com.kakarot.mall.product.service;

import com.kakarot.mall.product.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    List<CategoryVO> listEnabled();
}
