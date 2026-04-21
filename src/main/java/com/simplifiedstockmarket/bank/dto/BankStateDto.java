package com.simplifiedstockmarket.bank.dto;

import com.simplifiedstockmarket.common.StockPositionDto;

import java.util.List;

public record BankStateDto(List<StockPositionDto> stocks) {
}
