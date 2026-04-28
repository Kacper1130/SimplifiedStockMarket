package com.simplifiedstockmarket.wallet;

import com.simplifiedstockmarket.wallet.dto.WalletStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
class WalletController {

    private final WalletService walletService;

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletStateDto> getWallet(@PathVariable String walletId) {
        return ResponseEntity.ok(walletService.getWallet(walletId));
    }

    @GetMapping("/{walletId}/stocks/{stockId}")
    public ResponseEntity<Integer> getStockQuantity(@PathVariable String walletId, @PathVariable("stockId") String stockName) {
        return ResponseEntity.ok(walletService.getStockQuantity(walletId, stockName));
    }

}
