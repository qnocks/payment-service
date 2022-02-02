package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.AccountDto;
import org.springframework.stereotype.Service;

public interface AccountService {

    AccountDto getById(String id);
}
