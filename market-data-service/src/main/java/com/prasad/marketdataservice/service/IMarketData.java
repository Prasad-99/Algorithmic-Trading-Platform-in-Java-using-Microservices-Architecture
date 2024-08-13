package com.prasad.marketdataservice.service;

import com.prasad.marketdataservice.entity.MarketData;

import java.util.List;

public interface IMarketData {
    void updateData(MarketData marketData);
    void deleteOlderData(int window);
    List<MarketData> getAllData();
}
