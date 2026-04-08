package com.kakarot.mall.product.mapper;

import com.kakarot.mall.product.vo.SkuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductSkuMapper {

    List<SkuVO> selectByProductId(@Param("productId") Long productId);
}
