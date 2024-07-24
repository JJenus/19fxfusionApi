package com.tradeFx.tradeFx.marketData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market-data")
@CrossOrigin
public class MarketDataController {
    @Autowired
    private MarketDataService marketDataService;

    @GetMapping
    public ResponseEntity<List<MarketData>> getAllMarketDataService() {
        List<MarketData> historicalDataList = marketDataService.getAllMarketData();
        return ResponseEntity.ok(historicalDataList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketData> getMarketDataServiceById(@PathVariable Long id) {
        MarketData historicalData = marketDataService.getMarketDataById(id);
        return ResponseEntity.ok(historicalData);
    }

    @PostMapping
    public ResponseEntity<MarketData> createMarketDataService(@RequestBody MarketData historicalData) {
        MarketData createdData = marketDataService.createMarketData(historicalData);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketData> updateMarketDataService(@PathVariable Long id, @RequestBody MarketData updatedData) {
        MarketData updatedMarketDataService = marketDataService.updateMarketData(id, updatedData);
        return ResponseEntity.ok(updatedMarketDataService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarketDataService(@PathVariable Long id) {
        marketDataService.deleteMarketData(id);
        return ResponseEntity.ok().build();
    }
}
