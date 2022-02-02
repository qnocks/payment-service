package com.itransition.payment.core.dto;

import com.itransition.payment.core.domain.enums.TransactionStatus;
import lombok.Data;

@Data
public class TransactionInfoDto {

    private Long id;

    private String externalId;

    private TransactionStatus status;

    private String provider;

    private String additionalData;
}
