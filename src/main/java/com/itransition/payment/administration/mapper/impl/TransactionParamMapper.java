package com.itransition.payment.administration.mapper.impl;

import com.itransition.payment.administration.mapper.ParamMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionParamMapper implements ParamMapper {

    private static final String DTO_USER_ID_PARAM = "user";
    private static final String DTO_CREATED_AT_PARAM = "timestamp";
    private static final String DTO_EXTERNAL_DATE_PARAM = "providerTimestamp";
    private static final String ENTITY_USER_ID_PARAM = "userId";
    private static final String ENTITY_CREATED_AT_PARAM = "createdAt";
    private static final String ENTITY_EXTERNAL_DATE_PARAM = "externalDate";

    @Override
    public String map(String param) {
        switch (param) {
            case DTO_USER_ID_PARAM:
                return ENTITY_USER_ID_PARAM;
            case DTO_CREATED_AT_PARAM:
                return ENTITY_CREATED_AT_PARAM;
            case DTO_EXTERNAL_DATE_PARAM:
                return ENTITY_EXTERNAL_DATE_PARAM;
            default:
                return param;
        }
    }
}
