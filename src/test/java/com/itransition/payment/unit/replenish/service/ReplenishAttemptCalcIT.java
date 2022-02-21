package com.itransition.payment.unit.replenish.service;

import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.replenish.service.impl.ReplenishAttemptCalcImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

class ReplenishAttemptCalcIT extends AbstractIntegrationTest {

    @Autowired
    private ReplenishAttemptCalcImpl underTest;

    @Value("${test.threshold}")
    private int threshold;
    private int failedCount = 0;

    @Test
    void shouldCalcPossibilityToAnotherTry() {
        boolean expected = ++failedCount <= threshold;
        boolean actual = underTest.canAnotherTry();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldCalcNextAttemptTime() {
        double expected = Math.exp(failedCount);
        double actual = underTest.calcNextAttemptTime();
        assertThat(actual).isEqualTo(expected, 0.01);
    }
}
