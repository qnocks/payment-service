package com.itransition.payment.core.dto;

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
public class TransactionAdapterStateDto extends TransactionBaseDto {

    private AmountDto amount;
    private AmountDto commissionAmount;
    private String user;
}
