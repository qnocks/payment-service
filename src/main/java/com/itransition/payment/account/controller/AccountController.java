package com.itransition.payment.account.controller;

import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Controller", description = "API to check and validate Accounts")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Check existing account", description = "Indicates an account with provided id exists")
    @GetMapping("/{account_id}")
    public AccountDto checkExistingAccount(@PathVariable("account_id") String accountId) {
        return accountService.getById(accountId);
    }
}
