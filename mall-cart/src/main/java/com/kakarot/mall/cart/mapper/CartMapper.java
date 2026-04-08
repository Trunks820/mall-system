package com.kakarot.mall.cart.mapper;

import com.kakarot.mall.cart.entity.Cart;
import com.kakarot.mall.cart.vo.CartItemVO;
import com.kakarot.mall.cart.vo.SettlementItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {

    Cart selectByUserIdAndSkuId(@Param("userId") Long userId, @Param("skuId") Long skuId);

    int insert(Cart cart);

    int updateQuantityIncrement(@Param("id") Long id, @Param("quantity") Integer quantity);

    int updateQuantity(@Param("id") Long id, @Param("userId") Long userId, @Param("quantity") Integer quantity);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int updateChecked(@Param("id") Long id, @Param("userId") Long userId, @Param("checked") Integer checked);

    List<CartItemVO> selectCartListByUserId(@Param("userId") Long userId);

    List<SettlementItemVO> selectCheckedItemsByUserId(@Param("userId") Long userId);
}
