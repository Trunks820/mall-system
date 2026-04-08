package com.kakarot.mall.cart.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SettlementPreviewVO implements Serializable {

    private List<SettlementItemVO> items;

    private BigDecimal goodsTotalAmount;
}
