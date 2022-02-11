package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionUpdateDto;

public interface TransactionMapper {

    TransactionInfoDto toDto(Transaction transaction);

    Transaction toEntity(TransactionInfoDto infoDto);

    Transaction toEntity(TransactionAdapterStateDto adapterStateDto);

    Transaction toEntity(TransactionUpdateDto updateDto);
}
