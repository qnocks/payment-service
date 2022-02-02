package com.itransition.payment.core.dto;

import lombok.Data;

@Data
public class TransactionAdapterStateDto {

    private String externalId;

    private String provider;

    private AmountDto amount;

    private AmountDto commissionAmount;

    private String user;

    private String additionalData;
}
