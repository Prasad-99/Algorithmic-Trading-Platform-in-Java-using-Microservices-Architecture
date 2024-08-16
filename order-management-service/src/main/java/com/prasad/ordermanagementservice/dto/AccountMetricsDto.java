package com.prasad.ordermanagementservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountMetricsDto {
    private Long id;
    private double balance;
    private double unrealizedProfitLoss;
    private double positionSize;
    private String symbol;
    private LocalDateTime timestamp;
}
