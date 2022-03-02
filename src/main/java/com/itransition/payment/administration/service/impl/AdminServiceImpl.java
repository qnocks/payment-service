package com.itransition.payment.administration.service.impl;

import com.itransition.payment.administration.service.AdminService;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.type.TransactionStatus;
import com.itransition.payment.transaction.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final TransactionService transactionService;

    @Override
    public List<TransactionStateDto> searchTransactions(Pageable pageable) {
        return transactionService.getAll(pageable);
    }

    @Override
    public TransactionStateDto updateTransaction(TransactionStateDto adminDto) {
        return transactionService.update(adminDto);
    }

    @Override
    public TransactionStateDto completeTransaction(String externalId, String provider) {
        return transactionService.update(TransactionStateDto.builder()
                .externalId(externalId)
                .provider(provider)
                .status(TransactionStatus.COMPLETED)
                .build());
    }
}
