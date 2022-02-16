package com.itransition.payment.core.service;

import com.itransition.payment.core.domain.enums.ReplenishmentStatus;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    TransactionInfoDto save(TransactionStateDto stateDto);

    TransactionInfoDto update(TransactionInfoDto updateDto);

    TransactionStateDto update(TransactionStateDto adminDto);

    TransactionStateDto complete(String externalId, String provider);

    Boolean existsByExternalIdAndProvider(String externalId, String providerName);

    TransactionInfoDto getByExternalIdAndProvider(String externalId, String provider);

    List<TransactionStateDto> getAll();

    Optional<TransactionReplenishDto> findReadyToReplenish();

    void updateReplenishStatus(TransactionReplenishDto replenishDto, ReplenishmentStatus status);

    void setReplenishAfter(TransactionReplenishDto replenishDto, double replenishAfter);
}
