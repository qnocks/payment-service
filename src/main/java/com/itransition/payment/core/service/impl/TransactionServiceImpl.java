package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionAdminDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionUtil;
import com.itransition.payment.core.mapper.MapperUtil;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.PaymentProviderService;
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
    private final PaymentProviderService paymentProviderService;
    private final MapperUtil mapperUtil;
    private final ExceptionUtil exceptionUtil;

    @Override
    public TransactionInfoDto save(TransactionAdapterStateDto transactionAdapterStateDto) {
        Transaction transaction = mapperUtil.toEntity(transactionAdapterStateDto);
        initiateTransactionFields(transaction, transactionAdapterStateDto);
        transactionRepository.saveAndFlush(transaction);
        return mapperUtil.toDto(transaction);
    }

    private void initiateTransactionFields(Transaction transaction,
                                           TransactionAdapterStateDto transactionAdapterStateDto) {
        PaymentProvider provider = paymentProviderService.getByProvider(transactionAdapterStateDto.getProvider());

        if (provider != null) {
            transaction.setProvider(provider);
        }

        transaction.setStatus(TransactionStatus.INITIAL);
        transaction.setCreatedAt(LocalDateTime.now());
    }

    @Override
    public TransactionInfoDto update(TransactionInfoDto transactionInfoDto) {
        Transaction transaction = mapperUtil.toEntity(transactionInfoDto);
        updateTransactionField(transaction, transactionInfoDto.getProvider());

        Transaction existingTransaction = getById(transactionInfoDto.getId());

        BeanUtils.copyProperties(transaction, existingTransaction, BeanUtil.getNullPropertyNames(transaction));

        transactionRepository.save(existingTransaction);
        return mapperUtil.toDto(existingTransaction);
    }

    @Override
    public TransactionAdminDto update(TransactionAdminDto transactionAdminDto) {
        Transaction transaction = mapperUtil.toEntity(transactionAdminDto);
        updateTransactionField(transaction, transactionAdminDto.getProvider());

        Transaction existingTransaction = getById(transactionAdminDto.getId());

        BeanUtils.copyProperties(transaction, existingTransaction, BeanUtil.getNullPropertyNames(transaction));

        transactionRepository.save(existingTransaction);
        return mapperUtil.toAdminDto(existingTransaction);
    }

    @Override
    public TransactionAdminDto complete(String externalId, String provider) {
        return null;
    }

    private void updateTransactionField(Transaction transaction, String provider) {
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
