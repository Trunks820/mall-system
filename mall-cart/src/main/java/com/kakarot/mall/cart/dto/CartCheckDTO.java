package com.kakarot.mall.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartCheckDTO {

    @NotNull(message = "购物车项ID不能为空")
    private Long id;

    @NotNull(message = "勾选状态不能为空")
    private Integer checked;
}
