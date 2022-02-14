package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.dto.TransactionStateDto;
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
    public List<TransactionStateDto> searchTransactions(
            int page, int pageSize, String sort, String order, String value) {

        // TODO: Should be added pagination and sorting when main flow of payment service is over
        return transactionService.getAll();
    }

    @Override
    public TransactionStateDto updateTransaction(TransactionStateDto adminDto) {
        return transactionService.update(adminDto);
    }

    @Override
    public TransactionStateDto completeTransaction(String externalId, String provider) {
        return transactionService.complete(externalId, provider);
    }
}
