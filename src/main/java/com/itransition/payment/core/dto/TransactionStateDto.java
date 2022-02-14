package com.itransition.payment.core.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({
        "id", "externalId", "provider", "status",
        "amount", "commissionAmount", "user", "timestamp",
        "providerTimestamp", "additionalData"
})
public class TransactionStateDto extends TransactionBaseDto {

    private Long id;
    private TransactionStatus status;
    private AmountDto amount;
    private AmountDto commissionAmount;
    private String user;
    private Long timestamp;
    private Long providerTimestamp;
}
