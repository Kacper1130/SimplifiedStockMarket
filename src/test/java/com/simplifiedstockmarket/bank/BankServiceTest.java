package com.simplifiedstockmarket.bank;

import com.simplifiedstockmarket.bank.dto.BankStateDto;
import com.simplifiedstockmarket.common.StockPositionDto;
import com.simplifiedstockmarket.exception.OutOfStockException;
import com.simplifiedstockmarket.exception.StockNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @Mock
    private BankRespository bankRepository;
    @InjectMocks
    private BankService bankService;

    @Test
    void removeStock_whenStockExistsWithQuantity_shouldDecrement() {
        BankPosition position = new BankPosition("NVDA", 5);
        when(bankRepository.findById("NVDA")).thenReturn(Optional.of(position));

        bankService.removeStock("NVDA");

        ArgumentCaptor<BankPosition> captor = ArgumentCaptor.forClass(BankPosition.class);
        verify(bankRepository).save(captor.capture());
        assertThat(captor.getValue().getQuantity()).isEqualTo(4);
        assertThat(captor.getValue().getName()).isEqualTo("NVDA");
    }

    @Test
    void removeStock_whenStockNotFound_shouldThrowStockNotFoundException() {
        when(bankRepository.findById("NVDA")).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, () -> bankService.removeStock("NVDA"));
    }

    @Test
    void removeStock_whenQuantityIsZero_shouldThrowOutOfStockException() {
        BankPosition position = new BankPosition("NVDA", 0);
        when(bankRepository.findById("NVDA")).thenReturn(Optional.of(position));

        assertThrows(OutOfStockException.class, () -> bankService.removeStock("NVDA"));
    }

    @Test
    void addStock_whenStockExists_shouldIncrement() {
        BankPosition position = new BankPosition("NVDA", 3);
        when(bankRepository.findById("NVDA")).thenReturn(Optional.of(position));

        bankService.addStock("NVDA");

        ArgumentCaptor<BankPosition> captor = ArgumentCaptor.forClass(BankPosition.class);
        verify(bankRepository).save(captor.capture());
        assertThat(captor.getValue().getQuantity()).isEqualTo(4);
        assertThat(captor.getValue().getName()).isEqualTo("NVDA");
    }

    @Test
    void addStock_whenStockNotFound_shouldThrowStockNotFoundException() {
        when(bankRepository.findById("NVDA")).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, () -> bankService.addStock("NVDA"));
    }

    @Test
    void setBankState_shouldDeleteAllAndSaveNew() {
        BankStateDto dto = new BankStateDto(List.of(
                new StockPositionDto("NVDA", 10),
                new StockPositionDto("GOOG", 5)
        ));

        bankService.setBankState(dto);

        verify(bankRepository).deleteAll();
        verify(bankRepository, times(1)).saveAll(anyList());
    }
}