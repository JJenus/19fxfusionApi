package com.tradeFx.tradeFx.user;

import lombok.Data;

@Data
public class BalanceDTO {
    private String amount;
    private Action action;

    enum Action {
        ADD, SUB
    }
}
