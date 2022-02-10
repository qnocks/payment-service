package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionAdminDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    TransactionInfoDto save(TransactionAdapterStateDto transactionAdapterStateDto);

    TransactionInfoDto update(TransactionInfoDto transactionInfoDto);

    TransactionAdminDto update(TransactionAdminDto transactionAdminDto);

    TransactionAdminDto complete(String externalId, String provider);

    boolean existsByExternalId(String externalId);

    TransactionInfoDto getByExternalId(String externalId);

    List<TransactionInfoDto> getAllByExternalIdOrProvider(String externalId, String provider);

    List<TransactionAdminDto> getAll(Pageable pageable);
}
