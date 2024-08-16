package com.prasad.riskmanagementservice.controller;

import com.prasad.riskmanagementservice.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping("/position")
    public double getPostion() {
        return monitoringService.getPosition();
    }
    @GetMapping("/balance")
    public double getBalance() {
        return monitoringService.getBalance();
    }
    @GetMapping("/pnl")
    public double getPnl() {
        return monitoringService.getPnl();
    }
}
