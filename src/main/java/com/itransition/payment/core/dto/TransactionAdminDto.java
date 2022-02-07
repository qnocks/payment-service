package com.itransition.payment.core.dto;

import com.itransition.payment.core.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAdminDto {

    private Long id;
    private String externalId;
    private String provider;
    private TransactionStatus status;
    private AmountDto amount;
    private AmountDto commissionAmount;
    private String user;
    private Long timestamp;
    private Long providerTimestamp;
    private String additionalData;
}
