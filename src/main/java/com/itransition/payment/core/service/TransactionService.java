package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import java.util.List;

public interface TransactionService {

    TransactionInfoDto save(TransactionStateDto stateDto);

    TransactionInfoDto update(TransactionInfoDto updateDto);

    TransactionStateDto update(TransactionStateDto adminDto);

    TransactionStateDto complete(String externalId, String provider);

    Boolean existsByExternalIdAndProvider(String externalId, String providerName);

    TransactionInfoDto getByExternalIdAndProvider(String externalId, String provider);

    List<TransactionStateDto> getAll();
}
