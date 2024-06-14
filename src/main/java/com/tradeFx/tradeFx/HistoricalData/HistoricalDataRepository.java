package com.tradeFx.tradeFx.HistoricalData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricalDataRepository extends JpaRepository<HistoricalData, Long> {
}
