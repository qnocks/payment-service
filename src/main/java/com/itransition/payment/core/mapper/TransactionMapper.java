package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;

public interface TransactionMapper {

    TransactionInfoDto toDto(Transaction transaction);

    TransactionStateDto toAdminDto(Transaction transaction);

    TransactionReplenishDto toReplenishDto(Transaction transaction);

    Transaction toEntity(TransactionInfoDto infoDto);

    Transaction toEntity(TransactionStateDto stateDto);

    Transaction toEntity(TransactionReplenishDto replenishDto);
}
