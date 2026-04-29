package com.simplifiedstockmarket.chaos;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chaos")
class ChaosController {

    @PostMapping
    public void killInstance() {
        System.exit(1);
    }
}
