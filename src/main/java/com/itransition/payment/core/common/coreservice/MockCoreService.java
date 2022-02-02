package com.itransition.payment.core.common.coreservice;

import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.dto.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * This class represents mock for Core Services business logic
 *
 * After completed implementation of Payment Service
 * this class should be deleted and replaced with real services
 *
 */
@Slf4j
@Service
public class MockCoreService {

    public AccountDto checkExistingAccount(Long accountId) {
        AccountDto accountDto = new AccountDto("John", "Smith", null);

        log.info("IN checkExistingAccount - accountId: {}, returning: {}", accountId, accountDto);

        return accountDto;
    }

    public AuthResponse authorize() {
        AuthResponse authResponse =
                new AuthResponse("token", 5400L, 0L, "Bearer", 0, "profile email");

        log.info("IN authorize - returning AuthResponse: {}", authResponse);

        return authResponse;
    }
}
