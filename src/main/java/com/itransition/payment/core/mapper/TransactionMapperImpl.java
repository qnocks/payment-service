package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.AmountDto;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionAdminDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.service.AdminService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.SimpleTimeZone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionInfoDto toDto(Transaction transaction) {
        return TransactionInfoDto.builder()
                .id(transaction.getId())
                .externalId(transaction.getExternalId())
                .status(transaction.getStatus())
                .provider(transaction.getProvider().getName())
                .additionalData(transaction.getAdditionalData())
                .build();
    }

    @Override
    public TransactionAdminDto toAdminDto(Transaction transaction) {
        return TransactionAdminDto.builder()
                .id(transaction.getId())
                .externalId(transaction.getExternalId())
                .provider(transaction.getProvider().getName())
                .status(transaction.getStatus())
                .amount(AmountDto.builder()
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .build())
                .commissionAmount(AmountDto.builder()
                        .amount(transaction.getCommissionAmount())
                        .currency(transaction.getCommissionCurrency())
                        .build())
                .user(transaction.getUserId())
                .timestamp(transaction.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                .additionalData(transaction.getAdditionalData())
                .build();
    }

    @Override
    public Transaction toEntity(TransactionInfoDto transactionInfoDto) {
        return Transaction.builder()
                .id(transactionInfoDto.getId())
                .externalId(transactionInfoDto.getExternalId())
                .status(transactionInfoDto.getStatus())
                .provider(PaymentProvider.builder().name(transactionInfoDto.getProvider()).build())
                .additionalData(transactionInfoDto.getAdditionalData())
                .build();
    }

    @Override
    public Transaction toEntity(TransactionAdapterStateDto transactionAdapterStateDto) {
        return Transaction.builder()
                .externalId(transactionAdapterStateDto.getExternalId())
                .provider(PaymentProvider.builder()
                        .name(transactionAdapterStateDto.getProvider())
                        .build())
                .amount(transactionAdapterStateDto.getAmount().getAmount())
                .currency(transactionAdapterStateDto.getAmount().getCurrency())
                .commissionAmount(transactionAdapterStateDto.getCommissionAmount().getAmount())
                .commissionCurrency(transactionAdapterStateDto.getCommissionAmount().getCurrency())
                .userId(transactionAdapterStateDto.getUser())
                .additionalData(transactionAdapterStateDto.getAdditionalData())
                .build();
    }

    @Override
    public Transaction toEntity(TransactionAdminDto transactionAdminDto) {
        return Transaction.builder()
                .id(transactionAdminDto.getId())
                .externalId(transactionAdminDto.getExternalId())
                .provider(PaymentProvider.builder().name(transactionAdminDto.getProvider()).build())
                .status(transactionAdminDto.getStatus())
                .amount(transactionAdminDto.getAmount().getAmount())
                .currency(transactionAdminDto.getAmount().getCurrency())
                .commissionAmount(transactionAdminDto.getCommissionAmount().getAmount())
                .commissionCurrency(transactionAdminDto.getCommissionAmount().getCurrency())
                .userId(transactionAdminDto.getUser())
                .createdAt(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(transactionAdminDto.getTimestamp()), ZoneId.systemDefault()))
                .additionalData(transactionAdminDto.getAdditionalData())
                .build();
    }
}
