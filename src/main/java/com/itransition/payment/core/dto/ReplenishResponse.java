package com.itransition.payment.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplenishResponse {

    // TODO: Can be changed to more elegant way for retrieving info about replenishment request in the next commit
    //  otherwise this comment should be deleted
    private boolean success = true;
    private String error;
}
