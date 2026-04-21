package com.simplifiedstockmarket.wallet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Entity
@Getter
@Setter
class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private HashMap<String, Integer> stocks = new HashMap<>();

}
