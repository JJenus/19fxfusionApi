package com.tradeFx.tradeFx.HistoricalData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historical-data")
public class HistoricalDataController {
    @Autowired
    private HistoricalDataService historicalDataService;

    @GetMapping
    public ResponseEntity<List<HistoricalData>> getAllHistoricalData() {
        List<HistoricalData> historicalDataList = historicalDataService.getAllHistoricalData();
        return ResponseEntity.ok(historicalDataList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricalData> getHistoricalDataById(@PathVariable Long id) {
        HistoricalData historicalData = historicalDataService.getHistoricalDataById(id);
        return ResponseEntity.ok(historicalData);
    }

    @PostMapping
    public ResponseEntity<HistoricalData> createHistoricalData(@RequestBody HistoricalData historicalData) {
        HistoricalData createdData = historicalDataService.createHistoricalData(historicalData);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoricalData> updateHistoricalData(@PathVariable Long id, @RequestBody HistoricalData updatedData) {
        HistoricalData updatedHistoricalData = historicalDataService.updateHistoricalData(id, updatedData);
        return ResponseEntity.ok(updatedHistoricalData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHistoricalData(@PathVariable Long id) {
        historicalDataService.deleteHistoricalData(id);
        return ResponseEntity.ok().build();
    }
}

