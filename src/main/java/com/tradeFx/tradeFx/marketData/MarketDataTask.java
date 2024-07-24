package com.tradeFx.tradeFx.marketData;

import com.tradeFx.tradeFx.config.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@Configuration
public class MarketDataTask {
    @Autowired
    MarketDataService marketDataService;

    @Scheduled(fixedRate = 3000, initialDelay = 2000)
    private void fxTask(){
        List<MarketData> marketDataList = List.of(
                createMarketData(145, 1.063, 1.06302, 1.06289, 1.0631, 1.0628, 1676634420000L, 145),
                createMarketData(147, 1.0628, 1.0629, 1.0629, 1.063, 1.0626, 1676634480000L, 147),
                createMarketData(159, 1.063, 1.0629, 1.0629, 1.06305, 1.0627, 1676634540000L, 159),
                createMarketData(197, 1.0629, 1.06291, 1.0628, 1.063, 1.0627, 1676634600000L, 197),
                createMarketData(163, 1.0627, 1.06279, 1.06254, 1.06284, 1.0624, 1676634660000L, 163),
                createMarketData(180, 1.0626, 1.06255, 1.06259, 1.06265, 1.0623, 1676634720000L, 180),
                createMarketData(129, 1.0626, 1.06258, 1.06259, 1.06266, 1.0624, 1676634780000L, 129)
        );

        Random random = new Random();
        int randomIndex = random.nextInt(marketDataList.size());

        // Print the randomly selected MarketData
        MarketData randomData = marketDataList.get(randomIndex);
        marketDataService.broadcast(randomData);
    }

    private static MarketData createMarketData(double volume, double volumeWeighted, double open, double close, double high, double low, long timestamp, int transactions) {
        MarketData data = new MarketData();
        data.setCurrencyPair("EURUSD");
        data.setVolume(volume);
        data.setVolumeWeighted(volumeWeighted);
        data.setOpen(open);
        data.setClose(close);
        data.setHigh(high);
        data.setLow(low);
        data.setTimestamp(timestamp);
        data.setTransactions(transactions);
        return data;
    }
}
