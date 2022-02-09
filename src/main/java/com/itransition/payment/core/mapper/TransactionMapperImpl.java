package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
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
}
