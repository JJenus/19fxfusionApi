package com.tradeFx.tradeFx.trade;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tradeFx.tradeFx.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double lots;
    private TradeType tradeType;
    private double entryPrice;
    private double stopLossPrice;
    private double takeProfitPrice;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private double profitLoss;
    private ClosedBy closedBy;
    private Status status;

    @PrePersist
    private void onInsert(){
        openTime = LocalDateTime.now();
    }
}
