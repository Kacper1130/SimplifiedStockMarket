package com.simplifiedstockmarket.audit.dto;

import java.util.List;

public record AuditLogResponse(List<AuditLogDto> log) {
}
