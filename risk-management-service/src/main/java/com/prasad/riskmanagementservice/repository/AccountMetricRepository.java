package com.prasad.riskmanagementservice.repository;

import com.prasad.riskmanagementservice.entity.AccountMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMetricRepository extends JpaRepository<AccountMetrics, Long> {
}
