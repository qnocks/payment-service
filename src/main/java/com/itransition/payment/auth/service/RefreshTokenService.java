package com.itransition.payment.auth.service;

import com.itransition.payment.auth.entity.RefreshToken;
import com.itransition.payment.auth.entity.User;

public interface RefreshTokenService {

    RefreshToken getByToken(String token);

    RefreshToken createRefreshToken(User user);

    Boolean verifyAndDeleteExpired(RefreshToken token);

    void resetExpiration(RefreshToken token);
}
