package com.simplifiedstockmarket.audit;

import com.simplifiedstockmarket.bank.BankService;
import com.simplifiedstockmarket.bank.dto.BankStateDto;
import com.simplifiedstockmarket.common.StockOperationType;
import com.simplifiedstockmarket.common.StockPositionDto;
import com.simplifiedstockmarket.trade.dto.StockOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuditLogIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.9");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BankService bankService;
    @Autowired
    private AuditLogRepository auditLogRepository;

    @BeforeEach
    void setUp() {
        auditLogRepository.deleteAll();
        bankService.setBankState(new BankStateDto(List.of(
                new StockPositionDto("NVDA", 10)
        )));
    }

    @Test
    void getAuditLogs_shouldReturnEmptyListInitially() throws Exception {
        mockMvc.perform(get("/log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.log").isEmpty());
    }

    @Test
    void getAuditLogs_shouldReturnLogsInOrder() throws Exception {
        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.BUY))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.SELL))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.log[0].type").value("buy"))
                .andExpect(jsonPath("$.log[0].wallet_id").value("wallet1"))
                .andExpect(jsonPath("$.log[0].stock_name").value("NVDA"))
                .andExpect(jsonPath("$.log[1].type").value("sell"));
    }
}
