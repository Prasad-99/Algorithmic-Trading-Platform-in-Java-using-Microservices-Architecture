package com.prasad.ordermanagementservice.executor;

import com.prasad.ordermanagementservice.controller.BinanceApiController;
import com.prasad.ordermanagementservice.dto.SignalDto;
import com.prasad.ordermanagementservice.entity.OrderSummary;
import com.prasad.ordermanagementservice.service.BinanceApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class OMSRunner {

    private static final Logger log = LoggerFactory.getLogger(OMSRunner.class);

    public static OrderSummary runOrderManagementService(SignalDto signalDto) {
        BinanceApiController apiController = new BinanceApiController(new BinanceApiService());
        //receive signal
        Map<String, String> openOrderMap = apiController.getOpenOrders("BTCUSDT");
        if (!Objects.equals(openOrderMap.get("status"), "null")) {
            if ((openOrderMap.get("status").equals("PARTIALLY_FILLED") || openOrderMap.get("status").equals("NEW"))
                    && !openOrderMap.get("side").equals(signalDto.getSignal())) {
                log.info("Open order is partially filled or not filled and new signal is not same as previous signal. Cancelling the open order and placing a fresh order");
                apiController.cancelOrders("BTCUSDT");
                return apiController.placeOrder("BTCUSDT", signalDto.getSignal(), "LIMIT", "0.05", String.valueOf(signalDto.getPrice()));
            } else if ((openOrderMap.get("status").equals("PARTIALLY_FILLED") || openOrderMap.get("status").equals("NEW"))
                    && openOrderMap.get("side").equals(signalDto.getSignal())) {
                log.info("Open order is partially filled or not filled and new signal is same as previous signal. Keeping the order open.");
                return null;
            }
        } else if(Objects.equals(openOrderMap.get("status"), "null")){
                log.info("No open order. Placing a fresh order");
                return apiController.placeOrder("BTCUSDT", signalDto.getSignal(), "LIMIT", "0.05", String.valueOf(signalDto.getPrice()));
        } else {
            log.info("Error getting open order data.");
            return null;
        }
        return null;
        //check risk manager
        //check if order open
        //place order
        /*
        Monitor the open order -
            check if there is an open order if no then place fresh order else check the status
            if partially filled and new signal is same as previous signal then keep it open
            else cancel and place new order
         */
    }
}
