package com.simplifiedstockmarket.bank;

import com.simplifiedstockmarket.bank.dto.BankStateDto;
import com.simplifiedstockmarket.common.StockPositionDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BankIntegrationTest {

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

    @BeforeEach
    void setUp() {
        bankService.setBankState(new BankStateDto(List.of()));
    }

    @Test
    void getBankState_shouldReturnCurrentState() throws Exception {
        bankService.setBankState(new BankStateDto(List.of(
                new StockPositionDto("NVDA", 10)
        )));

        mockMvc.perform(get("/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stocks[0].name").value("NVDA"))
                .andExpect(jsonPath("$.stocks[0].quantity").value(10));
    }

    @Test
    void setBankState_shouldReturn200() throws Exception {
        mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BankStateDto(List.of(
                                new StockPositionDto("NVDA", 10)
                        )))))
                .andExpect(status().isOk());
    }

    @Test
    void setBankState_shouldReplaceExistingState() throws Exception {
        bankService.setBankState(new BankStateDto(List.of(
                new StockPositionDto("NVDA", 10)
        )));

        mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BankStateDto(List.of(
                                new StockPositionDto("GOOG", 5)
                        )))))
                .andExpect(status().isOk());

        assertThat(bankService.getBankState().stocks()).hasSize(1);
        assertThat(bankService.getBankState().stocks().get(0).name()).isEqualTo("GOOG");
    }
}
