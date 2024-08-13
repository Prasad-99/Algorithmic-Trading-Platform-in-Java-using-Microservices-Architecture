package com.prasad.marketdataservice.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasad.marketdataservice.entity.MarketData;
import com.prasad.marketdataservice.service.impl.MarketDataImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MarketDataClient {

    private BinanceApiClient binanceApiClient;
    private MarketDataImpl marketDataImpl;
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(MarketDataClient.class);

    @Scheduled(fixedRate = 5000)
    public void fetchMarketData(){
        String marketData = binanceApiClient.getMarketData();
        int smaWindow = 50;
        if(marketData != null){
            try {
                log.info(marketData);
                MarketData data = objectMapper.readValue(marketData, MarketData.class);
                data.setLocalDateTime(LocalDateTime.now());
                marketDataImpl.updateData(data);
                marketDataImpl.deleteOlderData(smaWindow);
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }
}
