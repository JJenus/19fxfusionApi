package com.tradeFx.tradeFx.marketData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketDataRepo extends JpaRepository<MarketData, Long> {
    // You can define custom query methods here if needed
}