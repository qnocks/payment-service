package com.itransition.payment.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private Integer expiresIn;
    private Integer refreshExpiresIn;
    private String tokenType;
    private Integer notBeforePolicy;
    private String scope;
}
