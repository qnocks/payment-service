package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionAdminDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    TransactionInfoDto save(TransactionAdapterStateDto adapterStateDto);

    TransactionInfoDto update(TransactionInfoDto infoDto);

    TransactionAdminDto update(TransactionAdminDto transactionAdminDto);

    TransactionAdminDto complete(String externalId, String provider);

    boolean existsByExternalIdAndProvider(String externalId, String providerName);

    TransactionInfoDto getByExternalIdAndProvider(String externalId, String provider);

    TransactionInfoDto getById(Long id);

    List<TransactionAdminDto> getAll(Pageable pageable);
}
