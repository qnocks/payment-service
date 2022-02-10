package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.dto.TransactionAdminDto;
import com.itransition.payment.core.service.AdminService;
import com.itransition.payment.core.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final TransactionService transactionService;

    @Override
    public List<TransactionAdminDto> searchTransactions(int page,
                                                        int pageSize,
                                                        String sort,
                                                        String order,
                                                        String value) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(
                new Sort.Order(Sort.Direction.valueOf(order), sort),
                new Sort.Order(Sort.Direction.valueOf(order), value)));
        return transactionService.getAll(pageable);
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
