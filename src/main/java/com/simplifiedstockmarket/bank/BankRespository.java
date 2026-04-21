package com.simplifiedstockmarket.bank;

import org.springframework.data.jpa.repository.JpaRepository;

interface BankRespository extends JpaRepository<BankPosition, String> {
}
