package com.itransition.payment.core.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountDto {

    private BigDecimal amount;

    private String currency;
}
