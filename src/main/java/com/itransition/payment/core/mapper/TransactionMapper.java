package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;

public interface TransactionMapper {

    TransactionInfoDto toDto(Transaction transaction);

    TransactionStateDto toAdminDto(Transaction transaction);

    Transaction toEntity(TransactionInfoDto infoDto);

    Transaction toEntity(TransactionStateDto stateDto);
}
