package com.itransition.payment.core.repository;

import com.itransition.payment.core.domain.PaymentProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {

    Optional<PaymentProvider> findByProvider(String provider);
}
