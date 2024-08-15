package com.prasad.tradeengineservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class SignalDto {
    private String signal;
    private String signalId;
    private Double price;
}
