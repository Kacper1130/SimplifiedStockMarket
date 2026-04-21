package com.simplifiedstockmarket.audit.dto;

import com.simplifiedstockmarket.common.StockOperationType;

public record AuditLogDto(StockOperationType type, String walletId, String stock_name) {
}
