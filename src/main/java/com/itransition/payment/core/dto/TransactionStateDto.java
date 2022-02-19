package com.itransition.payment.core.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.itransition.payment.core.types.TransactionStatus;
import com.itransition.payment.transaction.dto.AmountDto;
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

//    @ApiModelProperty(position = 0)
    private Long id;

//    @ApiModelProperty(notes = "transaction status", position = 2)
    private TransactionStatus status;

//    @ApiModelProperty(position = 4)
    private AmountDto amount;

//    @ApiModelProperty(position = 5)
    private AmountDto commissionAmount;

//    @ApiModelProperty(notes = "account id related to transaction", position = 6)
    private String user;

//    @ApiModelProperty(position = 7)
    private Long timestamp;

//    @ApiModelProperty(position = 8)
    private Long providerTimestamp;
}
