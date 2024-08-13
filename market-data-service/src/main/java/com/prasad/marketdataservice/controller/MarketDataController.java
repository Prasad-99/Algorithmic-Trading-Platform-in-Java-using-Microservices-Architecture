package com.prasad.marketdataservice.controller;

import com.prasad.marketdataservice.entity.MarketData;
import com.prasad.marketdataservice.service.IMarketData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MarketDataController {

    private IMarketData iMarketData;

    @GetMapping("/marketData")
    public List<MarketData> getAllMarketData() {
        return iMarketData.getAllData();
    }
}
