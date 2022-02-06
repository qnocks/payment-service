package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionUtil;
import com.itransition.payment.core.mapper.MapperUtil;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.TransactionService;
import com.itransition.payment.core.util.BeanUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final MapperUtil mapperUtil;
    private final ExceptionUtil exceptionUtil;

    @Override
    public TransactionInfoDto save(TransactionAdapterStateDto transactionAdapterStateDto) {
        Transaction transaction = mapperUtil.toEntity(transactionAdapterStateDto);
        initiateTransactionField(transaction);
        transactionRepository.saveAndFlush(transaction);
        return mapperUtil.toDto(transaction);
    }

    private void initiateTransactionField(Transaction transaction) {
        transaction.setStatus(TransactionStatus.INITIAL);
        transaction.setCreatedAt(LocalDateTime.now());
    }

    @Override
    public TransactionInfoDto update(TransactionInfoDto transactionInfoDto) {
        Transaction transaction = mapperUtil.toEntity(transactionInfoDto);

        // TODO: Should be changed to custom exception when implementation of exception handling
        Transaction existingTransaction = transactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        exceptionUtil.getMessage("transaction.cannot-get", transactionInfoDto.getId())));

        BeanUtils.copyProperties(transaction, existingTransaction, BeanUtil.getNullPropertyNames(transaction));

        transactionRepository.save(existingTransaction);
        return mapperUtil.toDto(existingTransaction);
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        return transactionRepository.existsByExternalId(externalId);
    }

    @Override
    public TransactionInfoDto getByExternalId(String externalId) {
        // TODO: Should be changed to custom exception when implementation of exception handling
        Transaction transaction = transactionRepository.findByExternalId(externalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        exceptionUtil.getMessage("transaction.cannot-get-by-external-id", externalId)));

        return mapperUtil.toDto(transaction);
    }

    @Override
    public List<TransactionInfoDto> getAllByExternalIdOrProvider(String externalId, String provider) {
        return transactionRepository
                .findAllByExternalIdAndProviderProvider(externalId, provider).stream()
                .map(mapperUtil::toDto)
                .collect(Collectors.toList());
    }
}
