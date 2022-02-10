package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.mapper.TransactionMapper;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.PaymentProviderService;
import com.itransition.payment.core.service.TransactionService;
import com.itransition.payment.core.util.BeansUtils;
import com.sun.istack.NotNull;
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
    private final ExceptionMessageResolver exceptionMessageResolver;

    @Transactional
    @Override
    public TransactionInfoDto save(TransactionAdapterStateDto adapterStateDto) {
        var transaction = transactionMapper.toEntity(adapterStateDto);
        initiateTransactionProvider(transaction, adapterStateDto.getProvider());
        transactionRepository.saveAndFlush(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Transactional
    @Override
    public TransactionInfoDto update(TransactionInfoDto infoDto) {
        var transaction = transactionMapper.toEntity(infoDto);
        initiateTransactionProvider(transaction, infoDto.getProvider());

        var existingTransaction = getTransactionById(transaction.getId());

        BeanUtils.copyProperties(transaction, existingTransaction, BeansUtils.getNullPropertyNames(transaction));

        transactionRepository.save(existingTransaction);
        return transactionMapper.toDto(existingTransaction);
    }

    private void initiateTransactionProvider(@NotNull Transaction transaction, String provider) {
        PaymentProvider paymentProvider = paymentProviderService.getByProvider(provider);

        if (paymentProvider != null) {
            transaction.setProvider(paymentProvider);
        }
    }

    private Transaction getTransactionById(Long id) {
        // TODO: Should be changed to custom exception when implementation of exception handling
        return transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                exceptionMessageResolver.getMessage("transaction.cannot-get", id)));
    }

    @Override
    public boolean existsByExternalIdAndProvider(String externalId, String providerName) {
        return transactionRepository.existsByExternalIdAndProviderName(externalId, providerName);
    }

    @Override
    public TransactionInfoDto getByExternalIdAndProvider(String externalId, String name) {
        Transaction transaction = transactionRepository.findByExternalIdAndProviderName(externalId, name)
                .orElseThrow(() -> new IllegalArgumentException(exceptionMessageResolver.getMessage(
                        "transaction.cannot-get-by-external-id-provider", externalId, name)));

        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionInfoDto getById(Long id) {
        Transaction transaction = getTransactionById(id);
        return transactionMapper.toDto(transaction);
    }
}
