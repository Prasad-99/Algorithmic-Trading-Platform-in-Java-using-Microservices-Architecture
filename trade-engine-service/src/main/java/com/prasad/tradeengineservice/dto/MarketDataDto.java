package com.prasad.tradeengineservice.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter@Setter@AllArgsConstructor@ToString@NoArgsConstructor
public class MarketDataDto {
    private Long id;
    private LocalDateTime localDateTime;
    private String symbol;
    private Double price;
}
