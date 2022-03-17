package com.itransition.payment.auth.service;

import com.itransition.payment.auth.dto.TokenAuthPayload;
import com.itransition.payment.auth.entity.User;

public interface SessionService {

    void createSession(User user, TokenAuthPayload token);

    void clearExpiredSessions();
}
