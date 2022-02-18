package com.itransition.payment.core.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.itransition.payment.core.types.TransactionStatus;
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
@JsonPropertyOrder({"id", "externalId", "status", "provider", "additionalData"})
public class TransactionInfoDto extends TransactionBaseDto {

    private Long id;
    private TransactionStatus status;
}

