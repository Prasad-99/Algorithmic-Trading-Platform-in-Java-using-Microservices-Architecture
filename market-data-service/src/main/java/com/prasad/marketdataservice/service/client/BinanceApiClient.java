package com.prasad.marketdataservice.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BinanceApiClient {

    private static final String BASE_URL = "https://api.binance.com/api/v3/ticker/price";
    private final Logger log = LoggerFactory.getLogger(MarketDataClient.class);

    public String getMarketData() {
        RestTemplate restTemplate = new RestTemplate();
        String symbol = "BTCUSDT";
        String url = String.format("%s?symbol=%s", BASE_URL, symbol);
        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("Response from Binance API is successful");
            return response;
        }catch (Exception e) {
            log.error("Exception occurred while retrieving data from Binance API");
            return null;
        }
    }
}
