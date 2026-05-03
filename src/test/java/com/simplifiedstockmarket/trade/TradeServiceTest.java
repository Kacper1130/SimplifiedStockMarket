package com.simplifiedstockmarket.trade;

import com.simplifiedstockmarket.audit.AuditLogService;
import com.simplifiedstockmarket.bank.BankService;
import com.simplifiedstockmarket.common.StockOperationType;
import com.simplifiedstockmarket.exception.OutOfStockException;
import com.simplifiedstockmarket.exception.StockNotFoundException;
import com.simplifiedstockmarket.exception.StockNotOwnedException;
import com.simplifiedstockmarket.trade.dto.StockOperation;
import com.simplifiedstockmarket.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private BankService bankService;
    @Mock
    private WalletService walletService;
    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private TradeService tradeService;

    @Test
    void buy_shouldRemoveFromBankAndAddToWallet() {
        tradeService.handleStockOperation("wallet1", "NVDA", new StockOperation(StockOperationType.BUY));

        verify(walletService).ensureWalletExists("wallet1");
        verify(bankService).removeStock("NVDA");
        verify(walletService).addStock("wallet1", "NVDA");
        verify(auditLogService).log(StockOperationType.BUY, "wallet1", "NVDA");
    }

    @Test
    void sell_shouldRemoveFromWalletAndAddToBank() {
        tradeService.handleStockOperation("wallet1", "NVDA", new StockOperation(StockOperationType.SELL));

        verify(walletService).ensureWalletExists("wallet1");
        verify(walletService).removeStock("wallet1", "NVDA");
        verify(bankService).addStock("NVDA");
        verify(auditLogService).log(StockOperationType.SELL, "wallet1", "NVDA");
    }

    @Test
    void buy_whenStockNotInBank_shouldThrowAndNotLog() {
        doThrow(new OutOfStockException()).when(bankService).removeStock("NVDA");
        StockOperation operation = new StockOperation(StockOperationType.BUY);

        assertThrows(OutOfStockException.class, () ->
                tradeService.handleStockOperation("wallet1", "NVDA", operation)
        );

        verify(auditLogService, never()).log(any(), any(), any());
    }

    @Test
    void sell_whenStockNotInWallet_shouldThrowAndNotLog() {
        doThrow(new StockNotOwnedException()).when(walletService).removeStock("wallet1", "NVDA");
        StockOperation operation = new StockOperation(StockOperationType.SELL);

        assertThrows(StockNotOwnedException.class, () ->
                tradeService.handleStockOperation("wallet1", "NVDA", operation)
        );

        verify(auditLogService, never()).log(any(), any(), any());
    }

    @Test
    void buy_whenStockDoesNotExist_shouldThrowAndNotLog() {
        doThrow(new StockNotFoundException()).when(bankService).removeStock("UNKNOWN");
        StockOperation operation = new StockOperation(StockOperationType.BUY);

        assertThrows(StockNotFoundException.class, () ->
                tradeService.handleStockOperation("wallet1", "UNKNOWN", operation)
        );

        verify(auditLogService, never()).log(any(), any(), any());
    }
}