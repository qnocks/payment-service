package com.itransition.payment.core.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AmountDto {

    private BigDecimal amount;

    private String currency;
}
