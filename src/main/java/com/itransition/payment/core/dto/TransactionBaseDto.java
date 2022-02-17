package com.itransition.payment.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionBaseDto {

    @ApiModelProperty(notes = "transaction id from payment provider", position = 1)
    private String externalId;

    @ApiModelProperty(notes = "payment provider type", position = 3)
    private String provider;

    @ApiModelProperty(position = 12)
    private String additionalData;
}
