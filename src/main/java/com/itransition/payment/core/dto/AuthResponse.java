package com.itransition.payment.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;

    private Long expiresIn;

    private Long refreshExpiresIn;

    private String tokenType;

    private Integer notBeforePolicy;

    private String scope;
}
