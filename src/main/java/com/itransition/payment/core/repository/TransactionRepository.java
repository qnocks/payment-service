package com.itransition.payment.core.repository;

import com.itransition.payment.core.domain.Transaction;
import java.util.List;
import java.util.Optional;
import liquibase.pro.packaged.S;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Boolean existsByExternalIdAndProviderName(String externalId, String name);

    Optional<Transaction> findByExternalId(String externalId);

    Optional<Transaction> findByExternalIdAndProviderName(String externalId, String name);
}
