package com.itransition.payment.core.repository;

import com.itransition.payment.core.domain.Transaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    boolean existsByExternalId(String externalId);

    Optional<Transaction> findByExternalId(String externalId);

    List<Transaction> findAllByExternalIdAndProvider(String externalId, String provider);
}