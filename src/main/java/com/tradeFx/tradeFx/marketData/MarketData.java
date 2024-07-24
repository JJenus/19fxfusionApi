package com.tradeFx.tradeFx.marketData;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String currencyPair;
    private Long currencyId;
    private double volume;
    private double volumeWeighted;
    private double open;
    private double close;
    private double high;
    private double low;
    private long timestamp;
    private int transactions;
}
