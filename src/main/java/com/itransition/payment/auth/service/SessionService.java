package com.itransition.payment.auth.service;

import com.itransition.payment.auth.dto.TokenPair;
import com.itransition.payment.auth.entity.User;

public interface SessionService {

    void createSession(User user, TokenPair token);

    void clearExpiredSessions();
}
