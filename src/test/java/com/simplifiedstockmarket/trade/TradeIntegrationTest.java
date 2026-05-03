package com.simplifiedstockmarket.trade;

import com.simplifiedstockmarket.audit.AuditLog;
import com.simplifiedstockmarket.audit.AuditLogRepository;
import com.simplifiedstockmarket.bank.BankService;
import com.simplifiedstockmarket.bank.dto.BankStateDto;
import com.simplifiedstockmarket.common.StockOperationType;
import com.simplifiedstockmarket.common.StockPositionDto;
import com.simplifiedstockmarket.trade.dto.StockOperation;
import com.simplifiedstockmarket.wallet.WalletRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TradeIntegrationTest {

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
    private WalletRepository walletRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
        auditLogRepository.deleteAll();
        bankService.setBankState(new BankStateDto(List.of(
                new StockPositionDto("NVDA", 10)
        )));
    }

    @Test
    void buy_whenStockAvailable_shouldReturn200() throws Exception {
        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.BUY))))
                .andExpect(status().isOk());
    }

    @Test
    void buy_whenStockNotInBank_shouldReturn400() throws Exception {
        bankService.setBankState(new BankStateDto(List.of(
                new StockPositionDto("NVDA", 0)
        )));

        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.BUY))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buy_whenStockDoesNotExist_shouldReturn404() throws Exception {
        mockMvc.perform(post("/wallets/wallet1/stocks/UNKNOWN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.BUY))))
                .andExpect(status().isNotFound());
    }

    @Test
    void sell_whenStockOwned_shouldReturn200() throws Exception {
        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.BUY))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.SELL))))
                .andExpect(status().isOk());
    }

    @Test
    void sell_whenStockNotOwned_shouldReturn400() throws Exception {
        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.SELL))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buy_shouldCreateWalletIfNotExists() throws Exception {
        mockMvc.perform(post("/wallets/newwallet/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.BUY))))
                .andExpect(status().isOk());

        assertThat(walletRepository.existsById("newwallet")).isTrue();
    }

    @Test
    void buy_shouldDecrementBankStock() throws Exception {
        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.BUY))))
                .andExpect(status().isOk());

        assertThat(bankService.getBankState().stocks().get(0).quantity()).isEqualTo(9);
    }

    @Test
    void buy_shouldLogSuccessfulOperation() throws Exception {
        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.BUY))))
                .andExpect(status().isOk());

        List<AuditLog> logs = auditLogRepository.findAll();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getType()).isEqualTo(StockOperationType.BUY);
        assertThat(logs.get(0).getWalletId()).isEqualTo("wallet1");
        assertThat(logs.get(0).getStockName()).isEqualTo("NVDA");
    }

    @Test
    void buy_whenFails_shouldNotLog() throws Exception {
        bankService.setBankState(new BankStateDto(List.of(
                new StockPositionDto("NVDA", 0)
        )));

        mockMvc.perform(post("/wallets/wallet1/stocks/NVDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockOperation(StockOperationType.BUY))))
                .andExpect(status().isBadRequest());

        assertThat(auditLogRepository.findAll()).isEmpty();
    }
}
