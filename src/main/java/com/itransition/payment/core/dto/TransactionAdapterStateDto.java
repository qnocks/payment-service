package com.itransition.payment.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAdapterStateDto {

    private String externalId;
    private String provider;
    private AmountDto amount;
    private AmountDto commissionAmount;
    private String user;
    private String additionalData;
}
