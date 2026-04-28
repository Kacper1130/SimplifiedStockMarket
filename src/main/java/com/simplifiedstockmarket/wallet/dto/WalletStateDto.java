package com.simplifiedstockmarket.wallet.dto;

import com.simplifiedstockmarket.common.StockPositionDto;

import java.util.List;

public record WalletStateDto(String walletId, List<StockPositionDto> stocks) {
}
