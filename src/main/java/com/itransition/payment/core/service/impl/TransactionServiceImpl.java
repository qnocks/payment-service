package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.domain.enums.ReplenishmentStatus;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.mapper.TransactionMapper;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.PaymentProviderService;
import com.itransition.payment.core.service.TransactionService;
import com.itransition.payment.core.util.BeansUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
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
    public TransactionInfoDto save(TransactionStateDto stateDto) {
        var transaction = transactionMapper.toEntity(stateDto);
        initiateTransactionProvider(transaction, stateDto.getProvider());
        transactionRepository.saveAndFlush(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Transactional
    @Override
    public TransactionInfoDto update(TransactionInfoDto updateDto) {
        var transaction = transactionMapper.toEntity(updateDto);
        Transaction updatedTransaction = processUpdate(transaction);
        return transactionMapper.toDto(updatedTransaction);
    }

    @Transactional
    @Override
    public TransactionStateDto update(TransactionStateDto adminDto) {
        var transaction = transactionMapper.toEntity(adminDto);
        Transaction updatedTransaction = processUpdate(transaction);
        return transactionMapper.toAdminDto(updatedTransaction);
    }

    private void initiateTransactionProvider(@NotNull Transaction transaction, String provider) {
        PaymentProvider paymentProvider = paymentProviderService.getByProvider(provider);

        if (paymentProvider != null) {
            transaction.setProvider(paymentProvider);
        }
    }

    private Transaction processUpdate(Transaction transaction) {
        initiateTransactionProvider(transaction, transaction.getProvider().getName());

        var existingTransaction = getTransactionByExternalIdAndProvider(
                transaction.getExternalId(),
                transaction.getProvider().getName());

        BeanUtils.copyProperties(transaction, existingTransaction, BeansUtils.getNullPropertyNames(transaction));

        transactionRepository.save(existingTransaction);
        return existingTransaction;
    }

    @Override
    public Boolean existsByExternalIdAndProvider(String externalId, String providerName) {
        return transactionRepository.existsByExternalIdAndProviderName(externalId, providerName);
    }

    @Transactional
    @Override
    public TransactionStateDto complete(String externalId, String provider) {
        var existingTransaction = getTransactionByExternalIdAndProvider(externalId, provider);
        existingTransaction.setStatus(TransactionStatus.COMPLETED);

        transactionRepository.saveAndFlush(existingTransaction);
        return transactionMapper.toAdminDto(existingTransaction);
    }

    @Override
    public TransactionInfoDto getByExternalIdAndProvider(String externalId, String name) {
        var transaction = getTransactionByExternalIdAndProvider(externalId, name);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public List<TransactionStateDto> getAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionReplenishDto getReadyToReplenish() {
        var transaction = transactionRepository.findAllByStatusAndReplenishmentStatusOrderByIdAsc(
                TransactionStatus.COMPLETED, ReplenishmentStatus.INITIAL)
                .stream()
                .filter(t -> t.getReplenishAfter() == null || t.getReplenishAfter().isBefore(LocalDateTime.now()))
                .findFirst()
                .orElse(null);

        return transactionMapper.toReplenishDto(transaction);
    }

    @Transactional
    @Override
    public void updateReplenishStatus(TransactionReplenishDto replenishDto, ReplenishmentStatus status) {
        var transaction = transactionMapper.toEntity(replenishDto);
        transaction.setReplenishmentStatus(status);
        processUpdate(transaction);
    }

    @Transactional
    @Override
    public void setReplenishAfter(TransactionReplenishDto replenishDto, double replenishAfter) {
        var transaction = transactionMapper.toEntity(replenishDto);
        transaction.setReplenishAfter(LocalDateTime.now().plusSeconds((long) replenishAfter));
        processUpdate(transaction);
    }


    private Transaction getTransactionByExternalIdAndProvider(String externalId, String name) {
        // TODO: Should be changed to custom exception when implementation of exception handling
        return transactionRepository.findByExternalIdAndProviderName(externalId, name)
                .orElseThrow(() -> new IllegalArgumentException(exceptionMessageResolver.getMessage(
                        "transaction.cannot-get-by-external-id-provider", externalId, name)));
    }
}
