package com.prasad.ordermanagementservice.executor;

import com.prasad.ordermanagementservice.controller.BinanceApiController;
import com.prasad.ordermanagementservice.dto.SignalDto;
import com.prasad.ordermanagementservice.entity.OrderSummary;
import com.prasad.ordermanagementservice.riskmanager.RiskMonitor;
import com.prasad.ordermanagementservice.service.BinanceApiService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
@AllArgsConstructor
public class OMSRunner {

    private static final Logger log = LoggerFactory.getLogger(OMSRunner.class);

    public static OrderSummary runOrderManagementService(SignalDto signalDto) {
        BinanceApiController apiController = new BinanceApiController(new BinanceApiService());
        RiskMonitor riskMonitor = new RiskMonitor(new RestTemplate());

        Map<String, String> openOrderMap = apiController.getOpenOrders("BTCUSDT");
        if (!Objects.equals(openOrderMap.get("status"), "null")) {
            if ((openOrderMap.get("status").equals("PARTIALLY_FILLED") || openOrderMap.get("status").equals("NEW"))
                    && !openOrderMap.get("side").equals(signalDto.getSignal())) {
                log.info("Open order is partially filled or not filled and new signal is not same as previous signal. " +
                        "Cancelling the open order and placing a fresh order");
                apiController.cancelOrders("BTCUSDT");

                return getOrderSummary(signalDto, apiController, riskMonitor);

            } else if ((openOrderMap.get("status").equals("PARTIALLY_FILLED") || openOrderMap.get("status").equals("NEW"))
                    && openOrderMap.get("side").equals(signalDto.getSignal())) {
                log.info("Open order is partially filled or not filled and new signal is same as previous signal. Keeping the order open.");
                return null;
            }
        } else if(Objects.equals(openOrderMap.get("status"), "null")){
                log.info("No open order. Placing a fresh order");
            return getOrderSummary(signalDto, apiController, riskMonitor);
        } else {
            log.info("Error getting open order data.");
            return null;
        }
        return null;
    }

    @Nullable
    private static OrderSummary getOrderSummary(SignalDto signalDto, BinanceApiController apiController, RiskMonitor riskMonitor) {
        if ((riskMonitor.monitorBalance() > 1000) && riskMonitor.monitorPosition() < Math.abs(9.5)) {
            return apiController.placeOrder("BTCUSDT", signalDto.getSignal(), "LIMIT", "0.05", String.valueOf(signalDto.getPrice()));
        } else if (riskMonitor.monitorPosition() > 9.5 && Objects.equals(signalDto.getSignal(), "SELL")){
            return apiController.placeOrder("BTCUSDT", signalDto.getSignal(), "LIMIT", "0.05", String.valueOf(signalDto.getPrice()));
        } else if (riskMonitor.monitorPosition() < -9.5 && Objects.equals(signalDto.getSignal(), "BUY")) {
            return apiController.placeOrder("BTCUSDT", signalDto.getSignal(), "LIMIT", "0.05", String.valueOf(signalDto.getPrice()));
        } else return null;
    }
}
