package com.itransition.payment.transaction.mapper;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TransactionParamsMapper {

    private static final String DEFAULT_PARAM = "id";
    private static final Map<String, String> MAPPED_PARAMS = Map.of(
            "user", "userId",
            "timestamp", "createdAt",
            "providerTimestamp", "externalDate"
    );

    public String map(String param) {
        var mappedParam = MAPPED_PARAMS.get(param);
        if (mappedParam == null) {
            return DEFAULT_PARAM;
        }

        return mappedParam;
    }
}
