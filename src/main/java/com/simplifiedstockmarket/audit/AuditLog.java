package com.simplifiedstockmarket.audit;

import com.simplifiedstockmarket.common.StockOperationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StockOperationType type;
    private String walletId;
    private String stockName;
    private LocalDateTime createdAt;

}
