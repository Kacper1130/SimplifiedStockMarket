package com.simplifiedstockmarket.bank;

import com.simplifiedstockmarket.bank.dto.BankStateDto;
import com.simplifiedstockmarket.common.StockPositionDto;
import com.simplifiedstockmarket.exception.OutOfStockException;
import com.simplifiedstockmarket.exception.StockNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRespository bankRepository;

    public BankStateDto getBankState() {
        return new BankStateDto(bankRepository.findAll()
                .stream()
                .map(bankPosition -> new StockPositionDto(bankPosition.getName(), bankPosition.getQuantity()))
                .toList()
        );
    }

    public String setBankState(BankStateDto bankStateDto) { //TODO cos innego powinna zwracac
        bankRepository.deleteAll();

        List<BankPosition> entities = bankStateDto.stocks().stream()
                .map(stockPositionDto -> new BankPosition(stockPositionDto.name(), stockPositionDto.quantity()))
                .toList();

        bankRepository.saveAll(entities);

        return "Success";
    }

    public void validateStockAvailability(String stockName) {
        BankPosition bankPosition = bankRepository.findById(stockName).orElseThrow(StockNotFoundException::new);

        if (bankPosition.getQuantity() <= 0) throw new OutOfStockException();
    }
}
