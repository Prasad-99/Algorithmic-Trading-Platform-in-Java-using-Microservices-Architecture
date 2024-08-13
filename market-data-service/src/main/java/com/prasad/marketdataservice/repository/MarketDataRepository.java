package com.prasad.marketdataservice.repository;

import com.prasad.marketdataservice.entity.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM market_data WHERE id IN " +
            "(SELECT id FROM (SELECT id FROM market_data ORDER BY local_date_time ASC LIMIT ?1) as temp)",
            nativeQuery = true)
    void deleteOlderRecords(int limit);
}
