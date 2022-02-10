package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionAdminDto;
import com.itransition.payment.core.dto.TransactionInfoDto;

public interface TransactionMapper {

    TransactionInfoDto toDto(Transaction transaction);

    TransactionAdminDto toAdminDto(Transaction transaction);

    Transaction toEntity(TransactionInfoDto infoDto);

    Transaction toEntity(TransactionAdapterStateDto adapterStateDto);

    Transaction toEntity(TransactionAdminDto adminDto);
}
