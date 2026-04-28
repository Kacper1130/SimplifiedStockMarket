package com.simplifiedstockmarket.wallet;

import com.simplifiedstockmarket.common.StockPositionDto;
import com.simplifiedstockmarket.exception.StockNotOwnedException;
import com.simplifiedstockmarket.exception.WalletNotFoundException;
import com.simplifiedstockmarket.wallet.dto.WalletStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletStateDto getWallet(String walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);

        List<StockPositionDto> stockList = wallet.getStocks().entrySet()
                .stream()
                .map(entry -> new StockPositionDto(entry.getKey(), entry.getValue()))
                .toList();

        return new WalletStateDto(wallet.getId(), stockList);
    }

    public Integer getStockQuantity(String walletId, String stockName) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);

        return wallet.getStocks().get(stockName); //TODO co jak nie ma takiej akcji
    }

    public void ensureWalletExists(String walletId) {
        if (!walletRepository.existsById(walletId)) {
            Wallet newWallet = new Wallet();
            newWallet.setId(walletId);
            walletRepository.save(newWallet);
        }
    }

    public void removeStock(String walletId, String stockName) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new IllegalStateException("Wallet not found"));

        Integer stockQuantity = wallet.getStocks().get(stockName);
        if (stockQuantity == null || stockQuantity <= 0) throw new StockNotOwnedException();

        int newQuantity = stockQuantity -1;
        if (newQuantity == 0) {
            wallet.getStocks().remove(stockName);
        } else {
            wallet.getStocks().put(stockName, newQuantity);
        }

        walletRepository.save(wallet);
    }

    public void addStock(String walletId, String stockName) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new IllegalStateException("Wallet not found"));

        int currentQuantity = wallet.getStocks().getOrDefault(stockName, 0);
        wallet.getStocks().put(stockName, currentQuantity + 1);

        walletRepository.save(wallet);
    }

}
