package com.itransition.payment.transaction.service.impl;

import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.type.ReplenishmentStatus;
import com.itransition.payment.core.type.TransactionStatus;
import com.itransition.payment.core.util.BeansUtils;
import com.itransition.payment.transaction.entity.Transaction;
import com.itransition.payment.transaction.mapper.TransactionMapper;
import com.itransition.payment.transaction.service.PaymentProviderService;
import com.itransition.payment.transaction.service.TransactionService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final PaymentProviderService paymentProviderService;
    private final TransactionMapper transactionMapper;
    private final ExceptionHelper exceptionHelper;

    @Transactional
    @Override
    public TransactionInfoDto save(@NotNull TransactionStateDto stateDto) {
        val transaction = transactionMapper.toEntity(stateDto);
        initiateTransactionProvider(transaction, stateDto.getProvider());
        transactionRepository.saveAndFlush(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Transactional
    @Override
    public TransactionInfoDto update(TransactionInfoDto updateDto) {
        val updatedTransaction = processUpdate(transactionMapper.toEntity(updateDto));
        return transactionMapper.toDto(updatedTransaction);
    }

    @Transactional
    @Override
    public TransactionStateDto update(TransactionStateDto adminDto) {
        val updatedTransaction = processUpdate(transactionMapper.toEntity(adminDto));
        return transactionMapper.toAdminDto(updatedTransaction);
    }

    @Override
    public Boolean existsByExternalIdAndProvider(String externalId, String providerName) {
        return transactionRepository.existsByExternalIdAndProviderName(externalId, providerName);
    }

    @Transactional
    @Override
    public TransactionStateDto complete(String externalId, String provider) {
        val existingTransaction = getTransactionByExternalIdAndProvider(externalId, provider);
        existingTransaction.setStatus(TransactionStatus.COMPLETED);

        transactionRepository.saveAndFlush(existingTransaction);
        return transactionMapper.toAdminDto(existingTransaction);
    }

    @Override
    public TransactionInfoDto getByExternalIdAndProvider(String externalId, String name) {
        val transaction = getTransactionByExternalIdAndProvider(externalId, name);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public List<TransactionStateDto> getAll(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .getContent().stream()
                .map(transactionMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionReplenishDto getReadyToReplenish() {
        val transaction = transactionRepository.findAllByStatusAndReplenishmentStatusOrderByIdAsc(
                TransactionStatus.COMPLETED, ReplenishmentStatus.INITIAL)
                .stream()
                .filter(t -> t.getReplenishAfter() == null || t.getReplenishAfter().isBefore(LocalDateTime.now()))
                .findFirst()
                .orElse(null);

        if (transaction == null) {
            return null;
        }

        return transactionMapper.toReplenishDto(transaction);
    }

    @Transactional
    @Override
    public void updateReplenishStatus(TransactionReplenishDto replenishDto, ReplenishmentStatus status) {
        val transaction = transactionMapper.toEntity(replenishDto);
        transaction.setReplenishmentStatus(status);
        processUpdate(transaction);
    }

    @Transactional
    @Override
    public void setReplenishAfter(TransactionReplenishDto replenishDto, double replenishAfter) {
        val transaction = transactionMapper.toEntity(replenishDto);
        transaction.setReplenishAfter(LocalDateTime.now().plusSeconds((long) replenishAfter));
        processUpdate(transaction);
    }

    private void initiateTransactionProvider(@NotNull Transaction transaction, String provider) {
        val paymentProvider = paymentProviderService.getByProvider(provider);

        if (paymentProvider != null) {
            transaction.setProvider(paymentProvider);
        }
    }

    private Transaction processUpdate(@NotNull Transaction transaction) {
        initiateTransactionProvider(transaction, transaction.getProvider().getName());

        val existingTransaction = getTransactionByExternalIdAndProvider(
                transaction.getExternalId(),
                transaction.getProvider().getName());

        BeanUtils.copyProperties(transaction, existingTransaction, BeansUtils.getNullPropertyNames(transaction));

        transactionRepository.save(existingTransaction);
        return existingTransaction;
    }

    private Transaction getTransactionByExternalIdAndProvider(String externalId, String name) {
        return transactionRepository.findByExternalIdAndProviderName(externalId, name)
                .orElseThrow(() -> exceptionHelper.buildTransactionException(
                        HttpStatus.NOT_FOUND,
                        "transaction.cannot-get-by-external-id-provider",
                        externalId, name));
    }
}
