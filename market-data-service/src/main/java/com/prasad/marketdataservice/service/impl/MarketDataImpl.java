package com.prasad.marketdataservice.service.impl;

import com.prasad.marketdataservice.entity.MarketData;
import com.prasad.marketdataservice.repository.MarketDataRepository;
import com.prasad.marketdataservice.service.IMarketData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MarketDataImpl implements IMarketData {

    private MarketDataRepository marketDataRepository;

    @Override
    public void updateData(MarketData marketData) {
        marketDataRepository.save(marketData);
    }

    @Override
    public void deleteOlderData(int window) {
        long count = marketDataRepository.count();
        if (count > window) {
            int recordsToDelete = (int) (count - window);
            marketDataRepository.deleteOlderRecords(recordsToDelete);
        }
    }

    @Override
    public List<MarketData> getAllData() {
        return marketDataRepository.findAll();
    }


}
