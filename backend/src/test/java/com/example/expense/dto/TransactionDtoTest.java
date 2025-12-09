package com.example.expense.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class TransactionDtoTest {

    @Test
    public void testTransactionDtoCreation() {
        TransactionDto dto = new TransactionDto();
        dto.description = "Test Expense";
        dto.amount = new BigDecimal("50.00");
        dto.category = "Food";
        dto.accountId = 1L;

        assertNotNull(dto);
        assertEquals("Test Expense", dto.description);
        assertEquals(new BigDecimal("50.00"), dto.amount);
        assertEquals("Food", dto.category);
        assertEquals(1L, dto.accountId);
    }
}
