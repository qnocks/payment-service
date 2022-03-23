package com.itransition.payment.auth.service;

import com.itransition.payment.auth.dto.TokenPayload;
import com.itransition.payment.auth.entity.User;

public interface SessionService {

    void createOrUpdate(User user, TokenPayload tokenPayload);

    void clearExpiredSessions();
}
