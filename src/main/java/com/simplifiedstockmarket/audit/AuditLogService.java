package com.simplifiedstockmarket.audit;

import com.simplifiedstockmarket.audit.dto.AuditLogDto;
import com.simplifiedstockmarket.audit.dto.AuditLogResponse;
import com.simplifiedstockmarket.common.StockOperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogResponse getAuditLogs() {
        List<AuditLog> logs = auditLogRepository.findAllByOrderByCreatedAtAsc();
        return new AuditLogResponse(
                logs.stream()
                        .map(auditLog -> new AuditLogDto(auditLog.getType(), auditLog.getWalletId(), auditLog.getStockName()))
                        .toList());
    }

    public void log(StockOperationType type, String walletId, String stockName) {
        auditLogRepository.save(new AuditLog(type, walletId, stockName));
    }
}
