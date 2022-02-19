package com.itransition.payment.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionBaseDto {

    private String externalId;
    private String provider;
    private String additionalData;
}
