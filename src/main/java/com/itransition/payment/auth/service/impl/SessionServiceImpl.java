package com.itransition.payment.auth.service.impl;

import com.itransition.payment.auth.dto.TokenAuthPayload;
import com.itransition.payment.auth.entity.Session;
import com.itransition.payment.auth.entity.User;
import com.itransition.payment.auth.repository.SessionRepository;
import com.itransition.payment.auth.service.SessionService;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public void createSession(User user, TokenAuthPayload tokenAuthPayload) {
        // TODO: before saving check if session with a user doesn't exists
        sessionRepository.save(Session.builder()
                .user(user)
                .token(tokenAuthPayload.getToken())
                .expired(tokenAuthPayload.getExpiration())
                .build());
    }

    @Scheduled(cron = "${app.auth.session.cron}")
    @Override
    public void clearExpiredSessions() {
        sessionRepository.deleteAll(sessionRepository.findAll().stream()
                .filter(session -> session.getExpired().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList()));
    }
}
