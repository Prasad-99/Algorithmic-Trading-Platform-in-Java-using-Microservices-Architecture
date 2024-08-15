package com.prasad.marketdataservice.service.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasad.marketdataservice.entity.MarketData;
import com.prasad.marketdataservice.service.impl.MarketDataImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class BinanceApiClient {

    private static final String BASE_URL = "https://testnet.binancefuture.com/fapi/v1/klines";
    private final Logger log = LoggerFactory.getLogger(MarketDataClient.class);

    @Autowired
    private MarketDataImpl marketDataImpl;

    public boolean getMarketData() {
        RestTemplate restTemplate = new RestTemplate();
        String symbol = "BTCUSDT";
        int limit = 30;
        String url = String.format("%s?symbol=%s&interval=%s&limit=%s", BASE_URL, symbol, "1m", limit);
        String response = restTemplate.getForObject(url, String.class);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);
            for (JsonNode node : jsonNode) {
                MarketData marketData = new MarketData();
                marketData.setLocalDateTime(LocalDateTime.now());
                marketData.setSymbol(symbol);
                marketData.setPrice(String.valueOf(node.get(4).asDouble()));
                marketDataImpl.updateData(marketData);
                marketDataImpl.deleteOlderData(limit);
                log.info(String.valueOf(node.get(4).asDouble()));
            }
            return true;
        } catch (Exception e) {
            log.error("Exception occurred while retrieving data from Binance API");
            return false;
        }
    }
}
