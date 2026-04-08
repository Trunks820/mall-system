package com.kakarot.mall.cart.controller;

import com.kakarot.mall.cart.dto.CartAddDTO;
import com.kakarot.mall.cart.dto.CartCheckDTO;
import com.kakarot.mall.cart.dto.CartDeleteDTO;
import com.kakarot.mall.cart.dto.CartUpdateQuantityDTO;
import com.kakarot.mall.cart.service.CartService;
import com.kakarot.mall.cart.vo.CartItemVO;
import com.kakarot.mall.cart.vo.SettlementPreviewVO;
import com.kakarot.mall.common.result.Result;
import com.kakarot.mall.infra.context.UserContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/app/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public Result<Void> add(@Valid @RequestBody CartAddDTO dto) {
        cartService.addCart(UserContext.getUserId(), dto);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<CartItemVO>> list() {
        return Result.success(cartService.listCart(UserContext.getUserId()));
    }

    @PostMapping("/updateQuantity")
    public Result<Void> updateQuantity(@Valid @RequestBody CartUpdateQuantityDTO dto) {
        cartService.updateQuantity(UserContext.getUserId(), dto);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@Valid @RequestBody CartDeleteDTO dto) {
        cartService.deleteCart(UserContext.getUserId(), dto);
        return Result.success();
    }

    @PostMapping("/check")
    public Result<Void> check(@Valid @RequestBody CartCheckDTO dto) {
        cartService.checkCart(UserContext.getUserId(), dto);
        return Result.success();
    }

    @PostMapping("/settlementPreview")
    public Result<SettlementPreviewVO> settlementPreview() {
        return Result.success(cartService.settlementPreview(UserContext.getUserId()));
    }
}
