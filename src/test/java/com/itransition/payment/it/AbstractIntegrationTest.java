package com.itransition.payment.it;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = {
        "auth.jwt.secret=test-secret-very-very-very-long",
        "auth.jwt.expired=123"
})
@AutoConfigureDataJpa
public abstract class AbstractIntegrationTest {
}

