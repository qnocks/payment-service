package com.itransition.payment.core.service;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.repository.PaymentProviderRepository;
import com.itransition.payment.core.service.impl.PaymentProviderServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentProviderServiceImplTest {

    @InjectMocks
    private PaymentProviderServiceImpl underTest;

    @Mock
    private PaymentProviderRepository paymentProviderRepository;

    @Test
    @DisplayName("Should map get PaymentProvider by name")
    void shouldGetPaymentProviderByName() {
        String providerName = "test";
        PaymentProvider expected = PaymentProvider.builder()
                .id(1L)
                .name(providerName)
                .build();

        when(paymentProviderRepository.findByName(providerName)).thenReturn(Optional.of(expected));

        PaymentProvider actual = underTest.getByProvider(providerName);

        verify(paymentProviderRepository, times(1)).findByName(providerName);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

}
