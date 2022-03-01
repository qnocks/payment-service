package com.itransition.payment.it.replenish.service;

import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.replenish.service.impl.ReplenishAttemptCalcImpl;
import lombok.val;
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
    private final int executionCount = 5;

    @BeforeAll
    void setup() {
        failedCount = 0;
    }

    @RepeatedTest(value = executionCount)
    void shouldCalcPossibilityToAnotherTry() {
        val actual = underTest.canAnotherTry();
        assertThat(actual).isEqualTo(calcPossibility());
    }

    @RepeatedTest(value = executionCount)
    void shouldCalcNextAttemptTime() {
        val actual = underTest.calcNextAttemptTime();
        assertThat(actual).isEqualTo(Math.exp(failedCount), Offset.offset(0.01));
    }

    private boolean calcPossibility() {
        val possible = ++failedCount <= threshold;
        if (!possible) {
            failedCount = 0;
        }

        return possible;
    }
}
