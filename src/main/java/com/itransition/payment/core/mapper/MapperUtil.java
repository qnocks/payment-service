package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperUtil implements TransactionMapper {

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
        mapper.typeMap(TransactionAdapterStateDto.class, Transaction.class).addMappings(mapping -> {
            mapping.skip(Transaction::setId);
            mapping.map((source -> source.getAmount().getAmount()), Transaction::setAmount);
            mapping.map((source -> source.getAmount().getCurrency()), Transaction::setCurrency);
            mapping.map((source -> source.getCommissionAmount().getAmount()), Transaction::setCommissionAmount);
            mapping.map((source -> source.getCommissionAmount().getCurrency()), Transaction::setCommissionCurrency);
            mapping.map(TransactionAdapterStateDto::getUser, Transaction::setUserId);
        });

        return mapper.map(transactionAdapterStateDto, Transaction.class);
    }
}
