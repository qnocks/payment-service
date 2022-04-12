package com.itransition.payment.administration.service.impl;

import com.itransition.payment.administration.mapper.ParamMapper;
import com.itransition.payment.administration.service.AdminService;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.type.TransactionStatus;
import com.itransition.payment.transaction.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final TransactionService transactionService;
    private final ParamMapper paramMapper;

    @Override
    public List<TransactionStateDto> searchTransactions(int page, int pageSize, String sort, String order) {
        val mappedSort = paramMapper.map(sort);
        return transactionService.getAll(PageRequest.of(page, pageSize, Sort.Direction.valueOf(order), mappedSort));
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
