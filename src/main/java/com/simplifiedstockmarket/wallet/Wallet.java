package com.simplifiedstockmarket.wallet;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
class Wallet {

    @Id
    private String id;
    @ElementCollection
    private Map<String, Integer> stocks = new HashMap<>();

}
