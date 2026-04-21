package com.simplifiedstockmarket.bank;

import com.simplifiedstockmarket.bank.dto.BankStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
class BankController {

    private final BankService bankService;

    @GetMapping
    public ResponseEntity<BankStateDto> getBankState() {
        return ResponseEntity.ok(bankService.getBankState());
    }

    @PostMapping
    public ResponseEntity<?> setBankState(@RequestBody BankStateDto bankState) {
        return ResponseEntity.ok(bankService.setBankState(bankState));
    }

}
