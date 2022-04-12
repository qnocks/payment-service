package com.itransition.payment.unit.administration.mapper;

import com.itransition.payment.administration.mapper.impl.TransactionParamMapper;
import lombok.val;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ParamMapperTest {

    @InjectMocks
    private TransactionParamMapper underTest;

    @ParameterizedTest
    @ValueSource(strings  = {
            "user,userId",
            "timestamp,createdAt",
            "providerTimestamp,externalDate"
    })
    void shouldMap(String givenExpectedPair) {
        val given = givenExpectedPair.split(",")[0];
        val expected = givenExpectedPair.split(",")[1];

        var actual = underTest.map(given);
        assertThat(actual).isEqualTo(expected);
    }
}
