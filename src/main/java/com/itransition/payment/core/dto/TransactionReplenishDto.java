package com.itransition.payment.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReplenishDto {

    private String provider;
    private String outerId;
    private String gateId;
    private LocalDateTime outerAt;
    private Integer account;
    private BigDecimal amount;
    private BigDecimal commissionAmount;
    private String orderId;
}
