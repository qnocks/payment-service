package com.itransition.payment.core.unit.service;

import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.transaction.repository.PaymentProviderRepository;
import com.itransition.payment.transaction.service.impl.PaymentProviderServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentProviderServiceTest {

    @InjectMocks
    private PaymentProviderServiceImpl underTest;

    @Mock
    private PaymentProviderRepository paymentProviderRepository;

    @Test
    void shouldGetPaymentProviderByName() {
        var expected = TestDataProvider.getPaymentProvider();

        when(paymentProviderRepository.findByName(expected.getName())).thenReturn(Optional.of(expected));

        var actual = underTest.getByProvider(expected.getName());

        verify(paymentProviderRepository, times(1)).findByName(expected.getName());
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    void shouldGetNull_when_PaymentProviderDoesntExist() {
        String providerName = "test";

        when(paymentProviderRepository.findByName(providerName)).thenReturn(Optional.empty());

        var actual = underTest.getByProvider(providerName);

        verify(paymentProviderRepository, times(1)).findByName(providerName);
        assertThat(actual).isNull();
    }
}
