package com.kakarot.mall.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDeleteDTO {

    @NotNull(message = "购物车项ID不能为空")
    private Long id;
}
