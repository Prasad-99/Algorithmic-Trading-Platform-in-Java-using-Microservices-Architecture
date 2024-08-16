package com.prasad.ordermanagementservice.liquidation;

import com.prasad.ordermanagementservice.riskmanager.RiskMonitor;
import com.prasad.ordermanagementservice.service.BinanceApiService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LiquidatePosition {
    private static final Logger log = LoggerFactory.getLogger(LiquidatePosition.class);
    private RiskMonitor riskMonitor;
    private BinanceApiService binanceApiService;

    @Scheduled(fixedRate = 1500)
    @Async
    public void monitorLiquidation() {
        try {
            Double pnl = riskMonitor.monitorUnrealizedProfit();
            log.info("Risk Manager - Profit/Loss data : {}", pnl);
            if (pnl < -500 || pnl > 50){
                Double pos = riskMonitor.monitorPosition();
                binanceApiService.placeMarketOrder("BTCUSDT", (pos > 0 ? "SELL" : "BUY"), "MARKET", pos.toString());
            }
        } catch (Exception e) {
            log.error("Error in monitorLiquidation", e);
        }
    }
}
