package com.simplifiedstockmarket.chaos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chaos")
class ChaosController {

    @PostMapping
    public ResponseEntity<String> killInstance() {
        System.exit(0);
        return ResponseEntity.ok("Instance killed"); //TODO nowy thread jakby nie bylo tej informacji
    }
}
