package com.kakarot.mall.cart.service.impl;

import com.kakarot.mall.cart.dto.CartAddDTO;
import com.kakarot.mall.cart.dto.CartCheckDTO;
import com.kakarot.mall.cart.dto.CartDeleteDTO;
import com.kakarot.mall.cart.dto.CartUpdateQuantityDTO;
import com.kakarot.mall.cart.entity.Cart;
import com.kakarot.mall.cart.mapper.CartMapper;
import com.kakarot.mall.cart.service.CartService;
import com.kakarot.mall.cart.vo.CartItemVO;
import com.kakarot.mall.cart.vo.SettlementItemVO;
import com.kakarot.mall.cart.vo.SettlementPreviewVO;
import com.kakarot.mall.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    @Override
    public void addCart(Long userId, CartAddDTO dto) {
        Cart existing = cartMapper.selectByUserIdAndSkuId(userId, dto.getSkuId());
        if (existing != null) {
            cartMapper.updateQuantityIncrement(existing.getId(), dto.getQuantity());
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(dto.getProductId());
            cart.setSkuId(dto.getSkuId());
            cart.setQuantity(dto.getQuantity());
            cartMapper.insert(cart);
        }
    }

    @Override
    public List<CartItemVO> listCart(Long userId) {
        return cartMapper.selectCartListByUserId(userId);
    }

    @Override
    public void updateQuantity(Long userId, CartUpdateQuantityDTO dto) {
        int rows = cartMapper.updateQuantity(dto.getId(), userId, dto.getQuantity());
        if (rows == 0) {
            throw new BizException("购物车项不存在");
        }
    }

    @Override
    public void deleteCart(Long userId, CartDeleteDTO dto) {
        int rows = cartMapper.deleteByIdAndUserId(dto.getId(), userId);
        if (rows == 0) {
            throw new BizException("购物车项不存在");
        }
    }

    @Override
    public void checkCart(Long userId, CartCheckDTO dto) {
        int rows = cartMapper.updateChecked(dto.getId(), userId, dto.getChecked());
        if (rows == 0) {
            throw new BizException("购物车项不存在");
        }
    }

    @Override
    public SettlementPreviewVO settlementPreview(Long userId) {
        List<SettlementItemVO> items = cartMapper.selectCheckedItemsByUserId(userId);
        if (items.isEmpty()) {
            throw new BizException("请至少选择一件商品");
        }

        BigDecimal goodsTotalAmount = BigDecimal.ZERO;
        for (SettlementItemVO item : items) {
            BigDecimal lineTotal = item.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setLineTotal(lineTotal);
            goodsTotalAmount = goodsTotalAmount.add(lineTotal);
        }

        SettlementPreviewVO vo = new SettlementPreviewVO();
        vo.setItems(items);
        vo.setGoodsTotalAmount(goodsTotalAmount);
        return vo;
    }
}
