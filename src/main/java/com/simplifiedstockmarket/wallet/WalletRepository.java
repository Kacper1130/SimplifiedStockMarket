package com.simplifiedstockmarket.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

interface WalletRepository extends JpaRepository<Wallet, String> {
}
