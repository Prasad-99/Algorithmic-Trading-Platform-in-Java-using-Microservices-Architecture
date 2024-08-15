package com.prasad.marketdataservice.service.client;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MarketDataClient {

    private BinanceApiClient binanceApiClient;
    private final Logger log = LoggerFactory.getLogger(MarketDataClient.class);

    @Scheduled(fixedRate = 59999)
    public void fetchMarketData(){
        if (binanceApiClient.getMarketData()){
            log.info("Market data fetched successfully");
        }
    }
}
