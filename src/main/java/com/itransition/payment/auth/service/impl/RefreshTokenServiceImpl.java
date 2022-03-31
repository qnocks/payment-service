package com.itransition.payment.auth.service.impl;

import com.itransition.payment.auth.entity.RefreshToken;
import com.itransition.payment.auth.entity.User;
import com.itransition.payment.auth.repository.RefreshTokenRepository;
import com.itransition.payment.auth.service.RefreshTokenService;
import com.itransition.payment.core.exception.ExceptionHelper;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${app.auth.jwt.token.refresh.expired}")
    private long expired;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ExceptionHelper exceptionHelper;

    @Override
    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> exceptionHelper.buildAuthException(
                        HttpStatus.UNAUTHORIZED, "auth.token.refresh.expired", token));
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        return refreshTokenRepository.save(RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expired(LocalDateTime.now().plusSeconds(expired))
                .user(user)
                .build());
    }

    @Override
    public Boolean verifyAndDeleteExpired(@NotNull RefreshToken token) {
        if (token.getExpired().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteById(token.getId());
            return false;
        }

        return true;
    }

    @Override
    public void resetExpiration(@NotNull RefreshToken token) {
        token.setExpired(LocalDateTime.now().plusSeconds(expired));
    }

    @Scheduled(cron = "${app.auth.jwt.token.refresh.cron}")
    public void clearExpiredTokens() {
        refreshTokenRepository.deleteAll(refreshTokenRepository.findAll().stream()
                .filter(token -> token.getExpired().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList()));
    }
}
