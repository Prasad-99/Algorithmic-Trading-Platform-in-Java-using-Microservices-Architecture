package com.prasad.ordermanagementservice.service;

import com.prasad.ordermanagementservice.entity.OrderSummary;
import com.prasad.ordermanagementservice.repository.OrderSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl {

    @Autowired
    public OrderSummaryRepository orderSummaryRepository;

    public void updateOrderSummary(OrderSummary orderSummary) {
            orderSummaryRepository.save(orderSummary);

    }
}
