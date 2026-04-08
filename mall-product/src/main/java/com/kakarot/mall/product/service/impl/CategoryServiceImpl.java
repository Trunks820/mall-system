package com.kakarot.mall.product.service.impl;

import com.kakarot.mall.product.mapper.CategoryMapper;
import com.kakarot.mall.product.service.CategoryService;
import com.kakarot.mall.product.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> listEnabled() {
        return categoryMapper.selectEnabledList();
    }
}
