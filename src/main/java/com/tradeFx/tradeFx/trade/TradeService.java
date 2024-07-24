package com.tradeFx.tradeFx.trade;

import com.tradeFx.tradeFx.notification.Notification;
import com.tradeFx.tradeFx.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    NotificationService notificationService;

    // Place a new trade
    public Trade placeTrade(Trade trade) {
        // Implement logic to calculate profit/loss, validate inputs, etc.
//        Trade trade = new Trade();
        // Set trade attributes based on tradeRequest
        // Save trade to repository
        trade.setStatus(Status.OPEN);
        Notification notification = Notification.builder()
                .title(String.format("%s Order ", trade.getCurrencyPair()))
                .message(String.format("Entry price: %.5f, Lots: %.2f", trade.getEntryPrice(), trade.getLots()))
                .userId(trade.getUser().getId()).build();

        notificationService.createNotification(notification);
        return tradeRepository.save(trade);
    }

    // Update stop loss level for a trade
    public Trade updateTrade(Long tradeId, Trade trade) {
        // Retrieve trade from repository
        Trade eTrade = tradeRepository.findById(tradeId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Trade not found"));
        // Update stop loss price
        eTrade.setStopLossPrice(trade.getStopLossPrice());
        eTrade.setTakeProfitPrice(trade.getTakeProfitPrice());

        // Save updated trade to repository
        return tradeRepository.save(eTrade);
    }

    // Update stop loss level for a trade
    public Trade updateStopLoss(Long tradeId, double stopLossPrice) {
        // Retrieve trade from repository
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Trade not found"));
        // Update stop loss price
        trade.setStopLossPrice(stopLossPrice);
        // Save updated trade to repository
        return tradeRepository.save(trade);
    }

    // Update take profit level for a trade
    public Trade updateTakeProfit(Long tradeId, double takeProfitPrice) {
        // Retrieve trade from repository
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Trade not found")
        );
        // Update take profit price
        trade.setTakeProfitPrice(takeProfitPrice);
        // Save updated trade to repository
        return tradeRepository.save(trade);
    }

    public Trade closeTrade(Long tradeId, double currentMarketPrice) {
        // Retrieve trade from repository
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Trade not found")
        );

        // Calculate profit/loss
        double entryPrice = trade.getEntryPrice();
        double profitLoss = (trade.getTradeType().equals(TradeType.LONG)) ?
                (currentMarketPrice - entryPrice) :
                (entryPrice - currentMarketPrice);
        trade.setProfitLoss(profitLoss);

        // Update status
        trade.setStatus(Status.CLOSED);

        // Save updated trade to repository
        return tradeRepository.save(trade);
    }
}

