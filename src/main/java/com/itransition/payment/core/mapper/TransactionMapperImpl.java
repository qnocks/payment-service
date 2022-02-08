package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapperImpl implements TransactionMapper {

    private final ModelMapper mapper;

    @Override
    public TransactionInfoDto toDto(Transaction transaction) {
        return mapper.map(transaction, TransactionInfoDto.class);
    }

    @Override
    public Transaction toEntity(TransactionInfoDto transactionInfoDto) {
        return mapper.map(transactionInfoDto, Transaction.class);
    }

    @Override
    public Transaction toEntity(TransactionAdapterStateDto transactionAdapterStateDto) {
        return Transaction.builder()
                .externalId(transactionAdapterStateDto.getExternalId())
                .provider(PaymentProvider.builder()
                        .provider(transactionAdapterStateDto.getProvider())
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
