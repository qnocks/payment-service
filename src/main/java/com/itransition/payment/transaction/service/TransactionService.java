package com.itransition.payment.transaction.service;

import com.itransition.payment.core.types.ReplenishmentStatus;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import java.util.List;

public interface TransactionService {

    TransactionInfoDto save(TransactionStateDto stateDto);

    TransactionInfoDto update(TransactionInfoDto updateDto);

    TransactionStateDto update(TransactionStateDto adminDto);

    TransactionStateDto complete(String externalId, String provider);

    Boolean existsByExternalIdAndProvider(String externalId, String providerName);

    TransactionInfoDto getByExternalIdAndProvider(String externalId, String provider);

    List<TransactionStateDto> getAll();

    TransactionReplenishDto getReadyToReplenish();

    void updateReplenishStatus(TransactionReplenishDto replenishDto, ReplenishmentStatus status);

    void setReplenishAfter(TransactionReplenishDto replenishDto, double replenishAfter);
}
