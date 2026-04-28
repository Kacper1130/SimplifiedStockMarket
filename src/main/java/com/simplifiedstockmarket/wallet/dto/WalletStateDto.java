package com.simplifiedstockmarket.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplifiedstockmarket.common.StockPositionDto;

import java.util.List;

public record WalletStateDto(@JsonProperty("id") String walletId, List<StockPositionDto> stocks) {
}
