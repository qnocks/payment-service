package com.itransition.payment.transaction.repository;

import com.itransition.payment.transaction.entity.PaymentProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {

    Optional<PaymentProvider> findByName(String name);
}
