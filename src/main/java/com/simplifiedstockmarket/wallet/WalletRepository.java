package com.simplifiedstockmarket.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
