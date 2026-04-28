package com.simplifiedstockmarket.trade;

import com.simplifiedstockmarket.StockOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class TradeController {

    private final TradeService tradeService;

    @PostMapping("/wallets/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<Void> handeStockOperation(@PathVariable("wallet_id") String walletId, @PathVariable("stock_name") String stockName, @RequestBody StockOperation type) {
        tradeService.handleStockOperation(walletId, stockName, type);
        return ResponseEntity.ok().build();
    }

}
