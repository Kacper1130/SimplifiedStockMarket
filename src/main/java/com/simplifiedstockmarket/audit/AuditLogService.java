package com.simplifiedstockmarket.audit;

import com.simplifiedstockmarket.audit.dto.AuditLogDto;
import com.simplifiedstockmarket.audit.dto.AuditLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogResponse getAuditLogs() {
        return new AuditLogResponse(auditLogRepository.findAllByOrderByIdAsc());
    }
}
