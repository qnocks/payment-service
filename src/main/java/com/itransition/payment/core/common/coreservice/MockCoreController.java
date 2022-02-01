package com.itransition.payment.core.common.coreservice;

import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
public class MockCoreController {

    private final MockCoreService mockCoreService;

    @GetMapping("/account/{account_id}")
    public ResponseEntity<AccountDto> checkExistingAccount(@PathVariable("account_id") Long accountId,
                                                           @RequestHeader HttpHeaders headers) {
        if (checkAuthorization(headers)) {
            return new ResponseEntity<>(mockCoreService.checkExistingAccount(accountId), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping(value = "/auth/token", params = {"grant_type", "client_secret", "client_id"})
    public ResponseEntity<AuthResponse> authorize() {
        return new ResponseEntity<>(mockCoreService.authorize(), HttpStatus.OK);
    }

    private boolean checkAuthorization(HttpHeaders headers) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        return "Bearer token".equals(token);
    }
}
