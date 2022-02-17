package com.itransition.payment.core.dto;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReplenishDto {

    @ApiModelProperty(notes = "payment provider type")
    private String provider;

    @ApiModelProperty(notes = "transaction id from payment provider")
    private String outerId;

    @ApiModelProperty(notes = "transaction id from payment service")
    private String gateId;

    @ApiModelProperty(notes = "payment date in payment provider")
    private LocalDateTime outerAt;

    @ApiModelProperty(notes = "account id related to transaction")
    private Integer account;
    private BigDecimal amount;
    private BigDecimal commissionAmount;
    private String orderId;
}
