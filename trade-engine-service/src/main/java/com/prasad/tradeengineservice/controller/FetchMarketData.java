package com.prasad.tradeengineservice.controller;

import com.prasad.tradeengineservice.dto.MarketDataDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class FetchMarketData {

    private final RestTemplate restTemplate;

    public List<MarketDataDto> fetchMarketData() {
        String url = "http://localhost:8080/api/marketData";
        MarketDataDto[] marketDataArray = restTemplate.getForObject(url, MarketDataDto[].class);

        return Arrays.asList(marketDataArray);
    }
}
