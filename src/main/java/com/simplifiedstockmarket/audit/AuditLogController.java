package com.simplifiedstockmarket.audit;

import com.simplifiedstockmarket.audit.dto.AuditLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<AuditLogResponse> getAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAuditLogs());
    }

}
