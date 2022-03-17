package com.itransition.payment.auth.service.impl;

import com.itransition.payment.auth.dto.TokenPayload;
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
    public void saveSession(User user, TokenPayload tokenPayload) {
        if (isSessionExists(user)) {
            updateSession(user, tokenPayload);
        } else {
            createSession(user, tokenPayload);
        }
    }

    @Scheduled(cron = "${app.auth.session.cron}")
    @Override
    public void clearExpiredSessions() {
        sessionRepository.deleteAll(sessionRepository.findAll().stream()
                .filter(session -> session.getExpired().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList()));
    }

    private void createSession(User user, TokenPayload tokenPayload) {
        sessionRepository.save(Session.builder()
                .user(user)
                .token(tokenPayload.getToken())
                .expired(tokenPayload.getExpiration())
                .build());
    }

    private void updateSession(User user, TokenPayload tokenPayload) {
        sessionRepository.findByUserId(user.getId()).ifPresent(session -> {
            session.setToken(tokenPayload.getToken());
            session.setExpired(tokenPayload.getExpiration());
            sessionRepository.save(session);
        });
    }

    private boolean isSessionExists(User user) {
        return sessionRepository.findByUserId(user.getId()).isPresent();
    }
}