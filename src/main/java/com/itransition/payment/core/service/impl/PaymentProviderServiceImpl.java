package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.repository.PaymentProviderRepository;
import com.itransition.payment.core.service.PaymentProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProviderServiceImpl implements PaymentProviderService {

    private final PaymentProviderRepository paymentProviderRepository;

    @Override
    public PaymentProvider getByProvider(String provider) {
        return paymentProviderRepository.findByProvider(provider).orElse(null);
    }
}
