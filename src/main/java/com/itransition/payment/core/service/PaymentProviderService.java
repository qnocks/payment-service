package com.itransition.payment.core.service;

import com.itransition.payment.core.domain.PaymentProvider;

public interface PaymentProviderService {

    PaymentProvider getByProvider(String provider);
}
