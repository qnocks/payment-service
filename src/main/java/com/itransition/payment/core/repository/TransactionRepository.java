package com.itransition.payment.core.repository;

import com.itransition.payment.transaction.entity.Transaction;
import com.itransition.payment.core.type.ReplenishmentStatus;
import com.itransition.payment.core.type.TransactionStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Boolean existsByExternalIdAndProviderName(String externalId, String name);

    Optional<Transaction> findByExternalIdAndProviderName(String externalId, String name);

    List<Transaction> findAllByStatusAndReplenishmentStatusOrderByIdAsc(
            TransactionStatus status, ReplenishmentStatus replenishmentStatus);
}
