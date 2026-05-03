package com.simplifiedstockmarket.audit;

import com.simplifiedstockmarket.audit.dto.AuditLogResponse;
import com.simplifiedstockmarket.common.StockOperationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    @Test
    void log_shouldSaveAuditLogWithCorrectFields() {
        auditLogService.log(StockOperationType.BUY, "wallet1", "NVDA");

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());
        assertThat(captor.getValue().getType()).isEqualTo(StockOperationType.BUY);
        assertThat(captor.getValue().getWalletId()).isEqualTo("wallet1");
        assertThat(captor.getValue().getStockName()).isEqualTo("NVDA");
    }

    @Test
    void getAuditLogs_shouldReturnLogsInOrder() {
        List<AuditLog> logs = List.of(
                new AuditLog(StockOperationType.BUY, "wallet1", "NVDA"),
                new AuditLog(StockOperationType.SELL, "wallet2", "GOOG")
        );
        when(auditLogRepository.findAllByOrderByCreatedAtAsc()).thenReturn(logs);

        AuditLogResponse response = auditLogService.getAuditLogs();

        assertThat(response.log()).hasSize(2);
        assertThat(response.log().get(0).type()).isEqualTo(StockOperationType.BUY);
        assertThat(response.log().get(0).walletId()).isEqualTo("wallet1");
        assertThat(response.log().get(0).stockName()).isEqualTo("NVDA");
        assertThat(response.log().get(1).type()).isEqualTo(StockOperationType.SELL);
        assertThat(response.log().get(1).walletId()).isEqualTo("wallet2");
        assertThat(response.log().get(1).stockName()).isEqualTo("GOOG");
    }

    @Test
    void getAuditLogs_whenNoLogs_shouldReturnEmptyList() {
        when(auditLogRepository.findAllByOrderByCreatedAtAsc()).thenReturn(List.of());

        AuditLogResponse response = auditLogService.getAuditLogs();

        assertThat(response.log()).isEmpty();
    }
}