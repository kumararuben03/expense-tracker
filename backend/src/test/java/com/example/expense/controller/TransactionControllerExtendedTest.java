package com.example.expense.controller;

import com.example.expense.domain.Transaction;
import com.example.expense.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerExtendedTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Test Transaction");
        transaction.setAmount(new BigDecimal("50.00"));
        transaction.setCategory("Food");
        transaction.setTimestamp(LocalDateTime.now());
    }

    @Test
    public void testGetAllTransactionsEndpoint() throws Exception {
        mockMvc.perform(get("/api/transactions?from=2024-01-01T00:00:00&to=2024-12-31T23:59:59"))
                .andExpect(status().isOk());
    }

    @Test
    public void testTransactionControllerExists() throws Exception {
        assertNotNull(transactionService);
    }

    @Test
    public void testGetTransactionWithDateRange() throws Exception {
        when(transactionService.list(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(transaction));
        
        mockMvc.perform(get("/api/transactions?from=2024-01-01T00:00:00&to=2024-12-31T23:59:59"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTransactionWithoutDateRange() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk());
    }
}
