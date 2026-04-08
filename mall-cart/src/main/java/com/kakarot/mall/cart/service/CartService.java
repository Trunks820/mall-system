package com.kakarot.mall.cart.service;

import com.kakarot.mall.cart.dto.CartAddDTO;
import com.kakarot.mall.cart.dto.CartCheckDTO;
import com.kakarot.mall.cart.dto.CartDeleteDTO;
import com.kakarot.mall.cart.dto.CartUpdateQuantityDTO;
import com.kakarot.mall.cart.vo.CartItemVO;
import com.kakarot.mall.cart.vo.SettlementPreviewVO;

import java.util.List;

public interface CartService {

    void addCart(Long userId, CartAddDTO dto);

    List<CartItemVO> listCart(Long userId);

    void updateQuantity(Long userId, CartUpdateQuantityDTO dto);

    void deleteCart(Long userId, CartDeleteDTO dto);

    void checkCart(Long userId, CartCheckDTO dto);

    SettlementPreviewVO settlementPreview(Long userId);
}
