package com.simplifiedstockmarket.trade;

import com.simplifiedstockmarket.StockOperation;
import com.simplifiedstockmarket.bank.BankService;
import com.simplifiedstockmarket.wallet.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class TradeService {

    private final BankService bankService;
    private final WalletService walletService;

    @Transactional
    public void handleStockOperation(String walletId, String stockName, StockOperation type) {
        walletService.ensureWalletExists(walletId);
        switch (type.type()) {
            case SELL -> handleSellOperation(walletId, stockName);
            case BUY -> handleBuyOperation(walletId, stockName);
        }
    }

    private void handleSellOperation(String walletId, String stockName) {
        walletService.removeStock(walletId, stockName);
        bankService.addStock(stockName);
    }

    private void handleBuyOperation(String walletId, String stockName) {
        bankService.removeStock(stockName);
        walletService.addStock(walletId, stockName);
    }

}
