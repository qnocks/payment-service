package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.TransactionAdminDto;
import com.itransition.payment.core.service.AdminService;
import com.itransition.payment.core.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final TransactionService transactionService;

    @Override
    public List<TransactionAdminDto> searchTransactions() {
        return null;
    }

    @Override
    public TransactionAdminDto updateTransaction(TransactionAdminDto transactionAdminDto) {
        return transactionService.update(transactionAdminDto);
    }

    @Override
    public TransactionAdminDto completeTransaction(String externalId, String provider) {
        return transactionService.complete(externalId, provider);
    }
}
