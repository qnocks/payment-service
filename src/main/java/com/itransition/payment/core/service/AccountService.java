package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.AccountDto;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    AccountDto getById(Long id);
}
