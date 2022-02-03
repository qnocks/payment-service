package com.itransition.payment.core.controller;

import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{account_id}")
    public AccountDto checkExistingAccount(@PathVariable("account_id") String accountId) {
        return accountService.getById(accountId);
    }
}
