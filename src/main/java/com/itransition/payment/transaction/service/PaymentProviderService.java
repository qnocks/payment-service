package com.itransition.payment.transaction.service;

import com.itransition.payment.transaction.entity.PaymentProvider;

public interface PaymentProviderService {

    PaymentProvider getByProvider(String provider);
}
