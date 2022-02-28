package com.itransition.payment.transaction.mapper;

import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.transaction.entity.Transaction;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapperImpl implements TransactionMapper {

    private final MapperFacade mapperFacade;

    @Override
    public TransactionInfoDto toDto(Transaction transaction) {
        return mapperFacade.map(transaction, TransactionInfoDto.class);
    }

    @Override
    public TransactionStateDto toAdminDto(Transaction transaction) {
        return mapperFacade.map(transaction, TransactionStateDto.class);
    }

    @Override
    public TransactionReplenishDto toReplenishDto(Transaction transaction) {
        return mapperFacade.map(transaction, TransactionReplenishDto.class);
    }

    @Override
    public Transaction toEntity(TransactionInfoDto infoDto) {
        return mapperFacade.map(infoDto, Transaction.class);
    }

    @Override
    public Transaction toEntity(TransactionStateDto stateDto) {
        return mapperFacade.map(stateDto, Transaction.class);
    }

    @Override
    public Transaction toEntity(TransactionReplenishDto replenishDto) {
        return mapperFacade.map(replenishDto, Transaction.class);
    }
}
