package com.tradeFx.tradeFx.marketData;

import com.tradeFx.tradeFx.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class MarketDataService {
    @Autowired
    private MarketDataRepo marketDataRepo;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void deleteMarketData(Long id) {
        marketDataRepo.deleteById(id);
    }

    public MarketData updateMarketData(Long id, MarketData updatedData) {
        return marketDataRepo.save(updatedData);
    }

    public List<MarketData> getAllMarketData() {
        return marketDataRepo.findAll();
    }

    public MarketData getMarketDataById(Long id) {
        return marketDataRepo.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Data not found"));
    }

    public MarketData createMarketData(MarketData historicalData) {
        return marketDataRepo.save(historicalData);
    }

    public List<MarketData> saveAll(List<MarketData> historicalData) {
        return marketDataRepo.saveAll(historicalData);
    }

    public void broadcast(MarketData marketData) {
        messagingTemplate.convertAndSend("/topic/fx", marketData);
    }
}
