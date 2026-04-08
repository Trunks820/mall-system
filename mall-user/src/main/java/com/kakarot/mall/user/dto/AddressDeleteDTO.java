package com.kakarot.mall.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDeleteDTO {

    @NotNull(message = "地址ID不能为空")
    private Long id;
}
