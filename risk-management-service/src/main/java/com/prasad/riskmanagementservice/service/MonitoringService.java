package com.prasad.riskmanagementservice.service;

import com.prasad.riskmanagementservice.entity.AccountMetrics;
import com.prasad.riskmanagementservice.repository.AccountMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoringService {

    @Autowired
    private AccountMetricRepository accountMetricsRepository;

    public double getBalance() {
        List<AccountMetrics> accountMetricsList = accountMetricsRepository.findAll();
        return accountMetricsList.get(0).getBalance();
    }

    public double getPosition() {
        List<AccountMetrics> accountMetricsList = accountMetricsRepository.findAll();
        return accountMetricsList.get(0).getPositionSize();
    }

    public double getPnl() {
        List<AccountMetrics> accountMetricsList = accountMetricsRepository.findAll();
        return accountMetricsList.get(0).getUnrealizedProfitLoss();
    }
}
