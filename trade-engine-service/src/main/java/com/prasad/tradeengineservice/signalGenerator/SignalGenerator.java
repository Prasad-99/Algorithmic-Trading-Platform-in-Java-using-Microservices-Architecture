package com.prasad.tradeengineservice.signalGenerator;

import com.prasad.tradeengineservice.constants.SignalConstants;
import com.prasad.tradeengineservice.controller.FetchMarketData;
import com.prasad.tradeengineservice.dto.MarketDataDto;
import com.prasad.tradeengineservice.entity.SignalData;
import com.prasad.tradeengineservice.repository.SignalDataRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SignalGenerator {
    private FetchMarketData fetchMarketData;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final StreamBridge streamBridge;
    private SignalDataRepository signalDataRepository;

    @Scheduled(fixedRate = 5000)
    public String generateSignal(){
        SignalData signalData = new SignalData();
        List<Double> prices = fetchData();
        String signal = evaluateSignal(prices);
        String signalId = generateSignalId(prices.get(prices.size()-1), signal);
        signalData.setSignalId(signalId);
        signalData.setSignalName(signal);
        signalData.setPrice(prices.get(prices.size()-1));
        signalData.setSymbol("BTCUSDT");
        signalDataRepository.save(signalData);
        var result = streamBridge.send("signal-sent", signal);
        log.debug("Signal sent to OMS? {}",result);
        return null;
    }

    private String evaluateSignal(List<Double> prices) {
        double simpleMovingAverage = (prices.stream().mapToDouble(Double::doubleValue).sum())/prices.size();
        Double currentPrice = prices.get(prices.size()-1);

        //If the current price is above the SMA, it indicates a potential uptrend or bullish market condition.
        if(currentPrice > simpleMovingAverage){
            log.info("SMA is {} and current price is {} - BUY Signal generated", simpleMovingAverage, currentPrice);
            return SignalConstants.BUY;
        }
        //If the current price is below the SMA, it indicates a potential downtrend or bearish market condition.
        else {
            log.info("SMA is {} and current price is {} - SELL Signal generated", simpleMovingAverage, currentPrice);
            return SignalConstants.SELL;
        }
    }

    private List<Double> fetchData() {
        List<MarketDataDto> marketDataDtos = fetchMarketData.fetchMarketData();
        List<Double> priceList = new ArrayList<>();
        log.info("Latest Market data fetched from market data service");
        marketDataDtos.forEach(marketDataDto -> {
            priceList.add(marketDataDto.getPrice());
        });
        return priceList;
    }

    private String generateSignalId(Double price, String signal){
        return LocalDateTime.now() +" "+ signal +" "+ price.toString();
    }
}
