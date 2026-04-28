package com.simplifiedstockmarket.audit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplifiedstockmarket.common.StockOperationType;

public record AuditLogDto(
        StockOperationType type,
        @JsonProperty("wallet_id") String walletId,
        @JsonProperty("stock_name") String stockName) {
}
