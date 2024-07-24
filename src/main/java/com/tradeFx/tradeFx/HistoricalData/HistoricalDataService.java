package com.tradeFx.tradeFx.HistoricalData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class HistoricalDataService {
    @Autowired
    private HistoricalDataRepository historicalDataRepository;

    public List<HistoricalData> getAllHistoricalData() {
        return historicalDataRepository.findAll();
    }

    public HistoricalData getHistoricalDataById(Long id) {
        return historicalDataRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Historical data not found with id: " + id));
    }

    public HistoricalData createHistoricalData(HistoricalData historicalData) {
        return historicalDataRepository.save(historicalData);
    }

    public HistoricalData updateHistoricalData(Long id, HistoricalData updatedData) {
        HistoricalData historicalData = getHistoricalDataById(id);
        historicalData.setTimestamp(updatedData.getTimestamp());
        historicalData.setOpen(updatedData.getOpen());
        historicalData.setHigh(updatedData.getHigh());
        historicalData.setLow(updatedData.getLow());
        historicalData.setClose(updatedData.getClose());
        historicalData.setVolume(updatedData.getVolume());
        historicalData.setTimeframe(updatedData.getTimeframe());
        return historicalDataRepository.save(historicalData);
    }

    public void deleteHistoricalData(Long id) {
        historicalDataRepository.deleteById(id);
    }
}

