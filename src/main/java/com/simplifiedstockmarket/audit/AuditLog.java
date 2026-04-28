package com.simplifiedstockmarket.audit;

import com.simplifiedstockmarket.common.StockOperationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StockOperationType type;
    private String walletId;
    private String stockName;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public AuditLog(StockOperationType type, String walletId, String stockName) {
        this.type = type;
        this.walletId = walletId;
        this.stockName = stockName;
    }

}
