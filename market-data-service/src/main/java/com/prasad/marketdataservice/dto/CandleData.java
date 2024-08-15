package com.prasad.marketdataservice.dto;

import lombok.*;

@Getter@Setter@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CandleData {
    private long openTime;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private long closeTime;
    private String quoteAssetVolume;
    private int numberOfTrades;
    private String takerBuyBaseAssetVolume;
    private String takerBuyQuoteAssetVolume;
    private String ignore;
}
