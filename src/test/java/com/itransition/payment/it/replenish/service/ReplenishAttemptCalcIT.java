package com.itransition.payment.it.replenish.service;

import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.replenish.service.impl.ReplenishAttemptCalcImpl;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReplenishAttemptCalcIT extends AbstractIntegrationTest {

    @Autowired
    private ReplenishAttemptCalcImpl underTest;

    @Value("${test.threshold}")
    private int threshold;
    private int failedCount;

    @BeforeAll
    void setup() {
        failedCount = 0;
    }

    @RepeatedTest(5)
    void shouldCalcPossibilityToAnotherTry() {
        boolean expected = calcPossibility();
        boolean actual = underTest.canAnotherTry();
        assertThat(actual).isEqualTo(expected);
    }

    @RepeatedTest(5)
    void shouldCalcNextAttemptTime() {
        double expected = Math.exp(failedCount);
        double actual = underTest.calcNextAttemptTime();
        assertThat(actual).isEqualTo(expected, Offset.offset(0.01));
    }

    private boolean calcPossibility() {
        boolean possible = ++failedCount <= threshold;
        if (!possible) {
            failedCount = 0;
        }

        return possible;
    }
}
