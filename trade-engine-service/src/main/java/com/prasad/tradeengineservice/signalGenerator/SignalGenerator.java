package com.prasad.tradeengineservice.signalGenerator;

import com.prasad.tradeengineservice.controller.FetchMarketData;
import com.prasad.tradeengineservice.dto.MarketDataDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SignalGenerator {
    private FetchMarketData fetchMarketData;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(fixedRate = 5000)
    public String generateSignal(){
        List<Double> prices = fetchData();
        double sumOfPrices = prices.stream().mapToDouble(Double::doubleValue).sum();
        Double currentPrice = prices.get(prices.size()-1);
        log.info("Current price: {}", currentPrice);
        log.info("Sum of prices: {}", sumOfPrices);
        return null;
    }

    private List<Double> fetchData() {
        List<MarketDataDto> marketDataDtos = fetchMarketData.fetchMarketData();
        List<Double> priceList = new ArrayList<>();
        log.info("Market data fetched from market data service");
        marketDataDtos.forEach(marketDataDto -> {
            log.info("Data {}", marketDataDto);
            priceList.add(marketDataDto.getPrice());
        });

        return priceList;
    }
}
