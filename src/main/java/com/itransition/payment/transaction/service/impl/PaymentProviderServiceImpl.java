package com.itransition.payment.transaction.service.impl;

import com.itransition.payment.transaction.entity.PaymentProvider;
import com.itransition.payment.transaction.repository.PaymentProviderRepository;
import com.itransition.payment.transaction.service.PaymentProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProviderServiceImpl implements PaymentProviderService {

    private final PaymentProviderRepository paymentProviderRepository;

    @Override
    public PaymentProvider getByProvider(String provider) {
        return paymentProviderRepository.findByName(provider).orElse(null);
    }
}
