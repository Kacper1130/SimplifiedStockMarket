package com.simplifiedstockmarket.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StockOperationType {
    @JsonProperty("buy")
    BUY,
    @JsonProperty("sell")
    SELL
}
