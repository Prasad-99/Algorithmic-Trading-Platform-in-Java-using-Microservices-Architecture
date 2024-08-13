package com.prasad.marketdataservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter@Setter@AllArgsConstructor@ToString@NoArgsConstructor
public class MarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime localDateTime;
    private String symbol;
    private Double price;

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = Double.parseDouble(price);
    }
}
