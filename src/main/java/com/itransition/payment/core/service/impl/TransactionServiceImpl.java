package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionAdminDto;
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
import org.springframework.data.domain.Pageable;
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
    public TransactionInfoDto update(TransactionInfoDto updateDto) {
        var transaction = transactionMapper.toEntity(updateDto);
        initiateTransactionProvider(transaction, updateDto.getProvider());

        var existingTransaction = getTransactionByExternalIdAndProvider(
                transaction.getExternalId(),
                transaction.getProvider().getName());

        BeanUtils.copyProperties(transaction, existingTransaction, BeansUtils.getNullPropertyNames(transaction));

        transactionRepository.save(existingTransaction);
        return transactionMapper.toDto(existingTransaction);
    }

    @Transactional
    @Override
    public TransactionAdminDto update(TransactionAdminDto transactionAdminDto) {
        Transaction transaction = transactionMapper.toEntity(transactionAdminDto);
        initiateTransactionProvider(transaction, transactionAdminDto.getProvider());

        Transaction existingTransaction = getTransactionById(transactionAdminDto.getId());

        BeanUtils.copyProperties(transaction, existingTransaction, BeansUtils.getNullPropertyNames(transaction));

        transactionRepository.saveAndFlush(existingTransaction);
        return transactionMapper.toAdminDto(existingTransaction);
    }

    private void initiateTransactionProvider(@NotNull Transaction transaction, String provider) {
        PaymentProvider paymentProvider = paymentProviderService.getByProvider(provider);

        if (paymentProvider != null) {
            transaction.setProvider(paymentProvider);
        }
    }

    private Transaction getById(Long id) {
        // TODO: Should be changed to custom exception when implementation of exception handling
        return transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                exceptionMessageResolver.getMessage("transaction.cannot-get", id)));
    }

    @Override
    public boolean existsByExternalIdAndProvider(String externalId, String providerName) {
        return transactionRepository.existsByExternalIdAndProviderName(externalId, providerName);
    public TransactionAdminDto complete(String externalId, String provider) {
        TransactionInfoDto infoDto = getByExternalIdAndProvider(externalId, provider);
        Transaction existingTransaction = getTransactionById(infoDto.getId());

        existingTransaction.setStatus(TransactionStatus.COMPLETED);

        transactionRepository.saveAndFlush(existingTransaction);
        return transactionMapper.toAdminDto(existingTransaction);
    }

    @Override
    public TransactionInfoDto getByExternalIdAndProvider(String externalId, String name) {
        var transaction = getTransactionByExternalIdAndProvider(externalId, name);
        return transactionMapper.toDto(transaction);
    }

    private Transaction getTransactionByExternalIdAndProvider(String externalId, String name) {
        return transactionRepository.findByExternalIdAndProviderName(externalId, name)
                .orElseThrow(() -> new IllegalArgumentException(exceptionMessageResolver.getMessage(
                        "transaction.cannot-get-by-external-id-provider", externalId, name)));
    }
}
