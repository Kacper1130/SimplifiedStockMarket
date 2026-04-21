package com.simplifiedstockmarket.wallet;

import com.simplifiedstockmarket.common.StockPositionDto;
import com.simplifiedstockmarket.exception.WalletNotFoundException;
import com.simplifiedstockmarket.wallet.dto.WalletStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletStateDto getWallet(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);

        List<StockPositionDto> stockList = wallet.getStocks().entrySet()
                .stream()
                .map(entry -> new StockPositionDto(entry.getKey(), entry.getValue()))
                .toList();

        return new WalletStateDto(wallet.getId(), stockList);
    }

    public Integer getStockQuantity(UUID walletId, String stockName) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);

        return wallet.getStocks().get(stockName); //TODO co jak nie ma takiej akcji
    }

    public void validateStockAvailability(UUID walletId, String stockName) {
        Wallet wallet = walletRepository.findById(walletId).orElseGet(() -> {
            Wallet newWallet = new Wallet();
            newWallet.setId(walletId);
            return walletRepository.save(newWallet);
        });

        int quantity = wallet.getStocks().getOrDefault(stockName, 0);

        if (quantity <= 0) throw new
    }
}
