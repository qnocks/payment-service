package com.itransition.payment.core.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
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
