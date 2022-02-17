package com.itransition.payment.account.controller;

import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.account.service.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@PropertySource("classpath:/messages/swagger/swagger.properties")
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "${swagger.account.checkExistingAccount}", response = AccountDto.class)
    @GetMapping("/{account_id}")
    public AccountDto checkExistingAccount(@PathVariable("account_id") String accountId) {
        return accountService.getById(accountId);
    }
}
