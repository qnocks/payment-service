package com.itransition.payment.replenish.repository;

import com.itransition.payment.core.entity.ReplenishError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplenishErrorRepository extends JpaRepository<ReplenishError, Long> {
}
