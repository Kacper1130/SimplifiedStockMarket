package com.simplifiedstockmarket.trade;

import com.simplifiedstockmarket.StockOperation;
import com.simplifiedstockmarket.bank.BankService;
import com.simplifiedstockmarket.wallet.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class TradeService {

    private final BankService bankService;
    private final WalletService walletService;

    @Transactional
    public void handleStockOperation(UUID walletId, String stockName, StockOperation type) {
        //todo sprawdzenie czy portfel istnieje i ewentualnie stworzyc
        switch (type.type()) {
            case SELL -> handleSellOperation(walletId, stockName);
            case BUY -> handleBuyOperation(walletId, stockName);
        }
    }

    private void handleSellOperation(UUID walletId, String stockName) {
        walletService.validateStockAvailability(walletId, stockName);
    }

    private void handleBuyOperation(UUID walletId, String stockName) {
        bankService.validateStockAvailability(stockName);
    }

}
