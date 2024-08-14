package com.prasad.tradeengineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradeEngineServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeEngineServiceApplication.class, args);
    }

}
