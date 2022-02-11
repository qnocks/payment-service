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
}
