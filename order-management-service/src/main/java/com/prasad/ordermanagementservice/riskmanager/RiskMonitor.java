package com.prasad.ordermanagementservice.riskmanager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
@Slf4j
public class RiskMonitor {
    private final RestTemplate restTemplate;

    public Double monitorUnrealizedProfit(){
        String url = "http://localhost:9010/pnl";
        Double pnl = restTemplate.getForObject(url, Double.class);
        return pnl;
    }

    public Double monitorPosition(){
        String url = "http://localhost:9010/position";
        Double position = restTemplate.getForObject(url, Double.class);
        return position;
    }

    public Double monitorBalance(){
        String url = "http://localhost:9010/balance";
        Double balance = restTemplate.getForObject(url, Double.class);
        if (balance < 1000){
            log.info("Balance is less than 1000. Top up your account");
        }
        return balance;
    }
}
