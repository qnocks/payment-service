package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionUtil;
import com.itransition.payment.core.mapper.TransactionMapper;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.PaymentProviderService;
import com.itransition.payment.core.service.TransactionService;
import com.itransition.payment.core.util.BeanUtil;
import com.sun.istack.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final PaymentProviderService paymentProviderService;
    private final TransactionMapper transactionMapper;
    private final ExceptionUtil exceptionUtil;

    @Transactional
    @Override
    public TransactionInfoDto save(TransactionAdapterStateDto transactionAdapterStateDto) {
        Transaction transaction = transactionMapper.toEntity(transactionAdapterStateDto);
        initiateTransactionProvider(transaction, transactionAdapterStateDto.getProvider());
        transactionRepository.saveAndFlush(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Transactional
    @Override
    public TransactionInfoDto update(TransactionInfoDto transactionInfoDto) {
        Transaction transaction = transactionMapper.toEntity(transactionInfoDto);
        initiateTransactionProvider(transaction, transactionInfoDto.getProvider());

        Transaction existingTransaction = getById(transaction.getId());

        BeanUtils.copyProperties(transaction, existingTransaction, BeanUtil.getNullPropertyNames(transaction));

        transactionRepository.save(existingTransaction);
        return transactionMapper.toDto(existingTransaction);
    }

    private void initiateTransactionProvider(@NotNull Transaction transaction, String provider) {
        PaymentProvider paymentProvider = paymentProviderService.getByProvider(provider);

        if (paymentProvider != null) {
            transaction.setProvider(paymentProvider);
        }
    }

    private Transaction getById(Long id) {
        // TODO: Should be changed to custom exception when implementation of exception handling
        return transactionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(exceptionUtil.getMessage("transaction.cannot-get", id)));
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

        return transactionMapper.toDto(transaction);
    }

    @Override
    public List<TransactionInfoDto> getAllByExternalIdOrProvider(String externalId, String provider) {
        return transactionRepository
                .findAllByExternalIdAndProviderProvider(externalId, provider).stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }
}
