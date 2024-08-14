package com.prasad.tradeengineservice.repository;

import com.prasad.tradeengineservice.entity.SignalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalDataRepository extends JpaRepository<SignalData, Long> {
}
