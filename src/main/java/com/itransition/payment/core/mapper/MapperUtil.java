package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.AmountDto;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionAdminDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
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
    public TransactionAdminDto toAdminDto(Transaction transaction) {
        Converter<Transaction, TransactionAdminDto> customConverter = new AbstractConverter<>() {
            @Override
            protected TransactionAdminDto convert(Transaction source) {
                return TransactionAdminDto.builder()
                        .amount(AmountDto.builder()
                                .amount(source.getAmount())
                                .currency(source.getCurrency())
                                .build())
                        .commissionAmount(AmountDto.builder()
                                .amount(source.getCommissionAmount())
                                .currency(source.getCommissionCurrency())
                                .build())
                        .build();
            }
        };

        mapper.typeMap(Transaction.class, TransactionAdminDto.class).addMappings(mapping -> {
            mapping.using(customConverter)
                    .map(Transaction::getCurrency, TransactionAdminDto::setAmount);
        });

        return mapper.map(transaction, TransactionAdminDto.class);
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

    @Override
    public Transaction toEntity(TransactionAdminDto transactionAdminDto) {
        mapper.typeMap(TransactionAdminDto.class, Transaction.class).addMappings(mapping -> {
            mapping.map((source -> source.getAmount().getAmount()), Transaction::setAmount);
            mapping.map((source -> source.getAmount().getCurrency()), Transaction::setCurrency);
            mapping.map(TransactionAdminDto::getUser, Transaction::setUserId);
        });

        return mapper.map(transactionAdminDto, Transaction.class);
    }
}
