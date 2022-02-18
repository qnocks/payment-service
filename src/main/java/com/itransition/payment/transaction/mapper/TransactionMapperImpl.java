package com.itransition.payment.transaction.mapper;

import com.itransition.payment.transaction.entity.PaymentProvider;
import com.itransition.payment.transaction.entity.Transaction;
import com.itransition.payment.transaction.dto.AmountDto;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionInfoDto toDto(Transaction transaction) {
        return TransactionInfoDto.builder()
                .id(transaction.getId())
                .externalId(transaction.getExternalId())
                .status(transaction.getStatus())
                .provider(transaction.getProvider().getName())
                .additionalData(transaction.getAdditionalData())
                .build();
    }

    @Override
    public TransactionStateDto toAdminDto(Transaction transaction) {
        return TransactionStateDto.builder()
                .id(transaction.getId())
                .externalId(transaction.getExternalId())
                .provider(transaction.getProvider().getName())
                .status(transaction.getStatus())
                .amount(AmountDto.builder()
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .build())
                .commissionAmount(AmountDto.builder()
                        .amount(transaction.getCommissionAmount())
                        .currency(transaction.getCommissionCurrency())
                        .build())
                .user(transaction.getUserId())
                .timestamp(transaction.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                .additionalData(transaction.getAdditionalData())
                .build();
    }

    @Override
    public TransactionReplenishDto toReplenishDto(Transaction transaction) {
        return TransactionReplenishDto.builder()
                .provider(transaction.getProvider().getName())
                .outerId(transaction.getExternalId())
                .gateId(String.valueOf(transaction.getId()))
                .outerAt(transaction.getExternalDate())
                .account(Integer.valueOf(transaction.getUserId()))
                .amount(transaction.getAmount())
                .commissionAmount(transaction.getCommissionAmount())
                .build();
    }

    @Override
    public Transaction toEntity(TransactionInfoDto infoDto) {
        return Transaction.builder()
                .id(infoDto.getId())
                .externalId(infoDto.getExternalId())
                .status(infoDto.getStatus())
                .provider(PaymentProvider.builder().name(infoDto.getProvider()).build())
                .additionalData(infoDto.getAdditionalData())
                .build();
    }

    @Override
    public Transaction toEntity(TransactionStateDto stateDto) {
        return Transaction.builder()
                .externalId(stateDto.getExternalId())
                .provider(PaymentProvider.builder().name(stateDto.getProvider()).build())
                .amount(stateDto.getAmount().getAmount())
                .currency(stateDto.getAmount().getCurrency())
                .commissionAmount(stateDto.getCommissionAmount().getAmount())
                .commissionCurrency(stateDto.getCommissionAmount().getCurrency())
                .userId(stateDto.getUser())
                .additionalData(stateDto.getAdditionalData())
                .build();
    }

    @Override
    public Transaction toEntity(TransactionReplenishDto replenishDto) {
        return Transaction.builder()
                .externalId(replenishDto.getOuterId())
                .provider(PaymentProvider.builder().name(replenishDto.getProvider()).build())
                .build();
    }

}
