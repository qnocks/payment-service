package com.itransition.payment.core.config;

import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.transaction.entity.Transaction;
import lombok.val;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaMapperConfiguration extends ConfigurableMapper {

    @Bean
    public MapperFacade mapperFacade() {
        return buildMapperFactory().getMapperFacade();
    }

    private MapperFactory buildMapperFactory() {
        val mapperFactory = new DefaultMapperFactory.Builder()
                .mapNulls(false)
                .build();

        configureTransactionMapping(mapperFactory);
        return mapperFactory;
    }

    private void configureTransactionMapping(MapperFactory factory) {
        factory.classMap(TransactionStateDto.class, Transaction.class)
                .field("amount.amount", "amount")
                .field("amount.currency", "currency")
                .field("commissionAmount.amount", "commissionAmount")
                .field("commissionAmount.currency", "commissionCurrency")
                .field("user", "userId")
                .field("provider", "provider.name")
                .byDefault()
                .register();

        factory.classMap(TransactionInfoDto.class, Transaction.class)
                .field("provider", "provider.name")
                .byDefault().register();

        factory.classMap(TransactionReplenishDto.class, Transaction.class)
                .field("provider", "provider.name")
                .field("outerId", "externalId")
                .field("gateId", "id")
                .field("outerAt", "externalDate")
                .field("account", "userId")
                .byDefault().register();
    }
}
