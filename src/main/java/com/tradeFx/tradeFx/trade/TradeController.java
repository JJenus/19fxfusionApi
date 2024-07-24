package com.tradeFx.tradeFx.trade;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trades")
@CrossOrigin
public class TradeController {
    @Autowired
    private TradeService tradeService;

    // Place a new trade
    @PostMapping("/place")
    public ResponseEntity<?> placeTrade(@RequestBody @Valid Trade tradeRequest) {
        Trade trade = tradeService.placeTrade(tradeRequest);
        return ResponseEntity.ok(trade);
    }

    // Update a trade
    @PutMapping("/{tradeId}")
    public ResponseEntity<?> updateTrade(@PathVariable Long tradeId, @RequestBody Trade trade) {
        Trade closedTrade = tradeService.updateTrade(tradeId, trade);
        return ResponseEntity.ok(closedTrade);
    }

    // Update stop loss level for a trade
    @PutMapping("/{tradeId}/stop-loss")
    public ResponseEntity<?> updateStopLoss(@PathVariable Long tradeId, @RequestParam double stopLossPrice) {
        Trade updatedTrade = tradeService.updateStopLoss(tradeId, stopLossPrice);
        return ResponseEntity.ok(updatedTrade);
    }

    // Update take profit level for a trade
    @PutMapping("/{tradeId}/take-profit")
    public ResponseEntity<?> updateTakeProfit(@PathVariable Long tradeId, @RequestParam double takeProfitPrice) {
        Trade updatedTrade = tradeService.updateTakeProfit(tradeId, takeProfitPrice);
        return ResponseEntity.ok(updatedTrade);
    }

    // Close a trade
    @PutMapping("/{tradeId}/close/{currentPrice}")
    public ResponseEntity<?> closeTrade(@PathVariable Long tradeId, @PathVariable Double currentPrice) {
        Trade closedTrade = tradeService.closeTrade(tradeId, currentPrice);
        return ResponseEntity.ok(closedTrade);
    }
}

