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
    public Transaction toEntity(TransactionInfoDto infoDto) {
        return Transaction.builder()
                .id(infoDto.getId())
                .externalId(infoDto.getExternalId())
                .status(infoDto.getStatus())
                .provider(PaymentProvider.builder().name(infoDto.getProvider()).build())
                .additionalData(infoDto.getAdditionalData())
                .build();
    }

    @Override
    public Transaction toEntity(TransactionAdapterStateDto adapterStateDto) {
        return Transaction.builder()
                .externalId(adapterStateDto.getExternalId())
                .provider(PaymentProvider.builder()
                        .name(adapterStateDto.getProvider())
                        .build())
                .amount(adapterStateDto.getAmount().getAmount())
                .currency(adapterStateDto.getAmount().getCurrency())
                .commissionAmount(adapterStateDto.getCommissionAmount().getAmount())
                .commissionCurrency(adapterStateDto.getCommissionAmount().getCurrency())
                .userId(adapterStateDto.getUser())
                .additionalData(adapterStateDto.getAdditionalData())
                .build();
    }

    @Override
    public Transaction toEntity(TransactionAdminDto adminDto) {
        return Transaction.builder()
                .id(adminDto.getId())
                .externalId(adminDto.getExternalId())
                .provider(PaymentProvider.builder().name(adminDto.getProvider()).build())
                .status(adminDto.getStatus())
                .amount(adminDto.getAmount().getAmount())
                .currency(adminDto.getAmount().getCurrency())
                .commissionAmount(adminDto.getCommissionAmount().getAmount())
                .commissionCurrency(adminDto.getCommissionAmount().getCurrency())
                .userId(adminDto.getUser())
                .createdAt(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(adminDto.getTimestamp()), ZoneId.systemDefault()))
                .additionalData(adminDto.getAdditionalData())
                .build();
    }
}
