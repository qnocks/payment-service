package com.itransition.payment.core.repository;

import com.itransition.payment.core.domain.ReplenishError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplenishErrorRepository extends JpaRepository<ReplenishError, Long> {
}
