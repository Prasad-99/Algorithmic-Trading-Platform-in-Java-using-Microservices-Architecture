package com.prasad.ordermanagementservice.controller;

import com.prasad.ordermanagementservice.service.BinanceApiService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class BinanceApiController {

    private BinanceApiService binanceApiService;
    private final Logger log = LoggerFactory.getLogger(BinanceApiController.class);

    @GetMapping("/placeOrder")
    public boolean placeOrder(
            @RequestParam String symbol,
            @RequestParam String side,
            @RequestParam String type,
            @RequestParam String quantity,
            @RequestParam String price) {
        try {
            log.info("Order Placed Successfully {}", binanceApiService.placeOrder(symbol, side, type, quantity, price));
            return true;
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
            return false;
        }
    }

    @GetMapping("/getOpenOrders")
    public Map<String, String> getOpenOrders(@RequestParam String symbol) {
        try {
            return binanceApiService.getOpenOrders(symbol);
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
            return null;
        }
    }

    @GetMapping("/cancelOrders")
    public boolean cancelOrders(@RequestParam String symbol) {
        try {
            binanceApiService.cancelOrder(symbol);
            return true;
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
            return false;
        }
    }
}
