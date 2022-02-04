package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public TransactionInfoDto save(TransactionAdapterStateDto transactionAdapterStateDto) {
        return null;
    }

    @Override
    public TransactionInfoDto update(TransactionInfoDto transactionInfoDto) {
        return null;
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        return false;
    }

    @Override
    public TransactionInfoDto getByExternalId(String externalId) {
        return null;
    }

    @Override
    public List<TransactionInfoDto> getAllByExternalIdOrProvider(String externalId, String provider) {
        return null;
    }
}
