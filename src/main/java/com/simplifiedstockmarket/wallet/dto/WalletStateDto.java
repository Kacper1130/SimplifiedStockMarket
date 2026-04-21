package com.simplifiedstockmarket.wallet.dto;

import com.simplifiedstockmarket.common.StockPositionDto;

import java.util.List;
import java.util.UUID;

public record WalletStateDto(UUID walletId, List<StockPositionDto> stocks) {
}
