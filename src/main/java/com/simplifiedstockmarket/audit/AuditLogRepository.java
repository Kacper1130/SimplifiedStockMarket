package com.simplifiedstockmarket.audit;

import com.simplifiedstockmarket.audit.dto.AuditLogDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLogDto> findAllByOrderByIdAsc();

}
