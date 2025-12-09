package com.example.expense.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionTest {
    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
    }

    @Test
    public void testTransactionCreation() {
        Transaction tx = Transaction.builder()
                .id(1L)
                .description("Test Expense")
                .amount(new BigDecimal("50.00"))
                .category("Food")
                .timestamp(LocalDateTime.now())
                .build();

        assertNotNull(tx);
        assertEquals(1L, tx.getId());
        assertEquals("Test Expense", tx.getDescription());
        assertEquals(new BigDecimal("50.00"), tx.getAmount());
        assertEquals("Food", tx.getCategory());
    }

    @Test
    public void testTransactionDefaultTimestamp() {
        transaction = new Transaction();
        assertNotNull(transaction.getTimestamp());
    }

    @Test
    public void testTransactionSetters() {
        transaction.setId(2L);
        transaction.setDescription("Shopping");
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setCategory("Retail");
        
        assertEquals(2L, transaction.getId());
        assertEquals("Shopping", transaction.getDescription());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals("Retail", transaction.getCategory());
    }

    @Test
    public void testTransactionAmountPrecision() {
        transaction.setAmount(new BigDecimal("99.99"));
        assertEquals(new BigDecimal("99.99"), transaction.getAmount());
    }

    @Test
    public void testTransactionMultipleCategories() {
        transaction.setCategory("Utilities");
        assertEquals("Utilities", transaction.getCategory());
        
        transaction.setCategory("Entertainment");
        assertEquals("Entertainment", transaction.getCategory());
    }
}
