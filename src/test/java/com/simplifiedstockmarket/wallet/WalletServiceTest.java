package com.simplifiedstockmarket.wallet;

import com.simplifiedstockmarket.exception.WalletNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void ensureWalletExists_whenWalletNotExists_shouldCreateIt() {
        when(walletRepository.existsById("wallet1")).thenReturn(false);

        walletService.ensureWalletExists("wallet1");

        ArgumentCaptor<Wallet> captor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo("wallet1");
    }

    @Test
    void ensureWalletExists_whenWalletExists_shouldNotCreateIt() {
        when(walletRepository.existsById("wallet1")).thenReturn(true);

        walletService.ensureWalletExists("wallet1");

        verify(walletRepository, never()).save(any());
    }

    @Test
    void removeStock_whenStockOwned_shouldDecrement() {
        Wallet wallet = new Wallet();
        wallet.setId("wallet1");
        wallet.getStocks().put("NVDA", 3);
        when(walletRepository.findById("wallet1")).thenReturn(Optional.of(wallet));

        walletService.removeStock("wallet1", "NVDA");

        ArgumentCaptor<Wallet> captor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository).save(captor.capture());
        assertThat(captor.getValue().getStocks()).containsEntry("NVDA", 2);
    }

    @Test
    void removeStock_whenLastStock_shouldRemoveEntry() {
        Wallet wallet = new Wallet();
        wallet.setId("wallet1");
        wallet.getStocks().put("NVDA", 1);
        when(walletRepository.findById("wallet1")).thenReturn(Optional.of(wallet));

        walletService.removeStock("wallet1", "NVDA");

        ArgumentCaptor<Wallet> captor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository).save(captor.capture());
        assertThat(captor.getValue().getStocks()).doesNotContainKey("NVDA");
    }

    @Test
    void addStock_whenStockAlreadyOwned_shouldIncrement() {
        Wallet wallet = new Wallet();
        wallet.setId("wallet1");
        wallet.getStocks().put("NVDA", 2);
        when(walletRepository.findById("wallet1")).thenReturn(Optional.of(wallet));

        walletService.addStock("wallet1", "NVDA");

        ArgumentCaptor<Wallet> captor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository).save(captor.capture());
        assertThat(captor.getValue().getStocks()).containsEntry("NVDA", 3);
    }

    @Test
    void addStock_whenStockNotOwned_shouldAddWithQuantityOne() {
        Wallet wallet = new Wallet();
        wallet.setId("wallet1");
        when(walletRepository.findById("wallet1")).thenReturn(Optional.of(wallet));

        walletService.addStock("wallet1", "NVDA");

        ArgumentCaptor<Wallet> captor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository).save(captor.capture());
        assertThat(captor.getValue().getStocks()).containsEntry("NVDA", 1);
    }

    @Test
    void getWallet_whenWalletNotFound_shouldThrowWalletNotFoundException() {
        when(walletRepository.findById("wallet1")).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () ->
                walletService.getWallet("wallet1")
        );
    }
}