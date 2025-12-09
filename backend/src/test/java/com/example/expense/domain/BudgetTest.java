package com.example.expense.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class BudgetTest {
    private Budget budget;

    @BeforeEach
    public void setUp() {
        budget = new Budget();
    }

    @Test
    public void testBudgetCreation() {
        budget.setCategory("Food");
        budget.setLimitAmount(new BigDecimal("500.00"));
        budget.setMonth(1);
        budget.setYear(2024);
        
        assertEquals("Food", budget.getCategory());
        assertEquals(new BigDecimal("500.00"), budget.getLimitAmount());
        assertEquals(1, budget.getMonth());
        assertEquals(2024, budget.getYear());
    }

    @Test
    public void testBudgetSettersAndGetters() {
        budget.setCategory("Entertainment");
        budget.setLimitAmount(new BigDecimal("200.00"));
        budget.setMonth(5);
        budget.setYear(2024);
        
        assertEquals("Entertainment", budget.getCategory());
        assertEquals(new BigDecimal("200.00"), budget.getLimitAmount());
        assertEquals(5, budget.getMonth());
        assertEquals(2024, budget.getYear());
    }

    @Test
    public void testBudgetWithMultipleCategories() {
        budget.setCategory("Utilities");
        budget.setLimitAmount(new BigDecimal("150.00"));
        
        assertEquals("Utilities", budget.getCategory());
        assertEquals(new BigDecimal("150.00"), budget.getLimitAmount());
    }

    @Test
    public void testBudgetMonthYearValidation() {
        budget.setMonth(12);
        budget.setYear(2024);
        
        assertEquals(12, budget.getMonth());
        assertEquals(2024, budget.getYear());
    }

    @Test
    public void testBudgetBuilder() {
        Budget builtBudget = Budget.builder()
                .id(5L)
                .category("Transport")
                .limitAmount(new BigDecimal("300.00"))
                .month(6)
                .year(2024)
                .build();
        
        assertEquals(5L, builtBudget.getId());
        assertEquals("Transport", builtBudget.getCategory());
        assertEquals(new BigDecimal("300.00"), builtBudget.getLimitAmount());
        assertEquals(6, builtBudget.getMonth());
        assertEquals(2024, builtBudget.getYear());
    }

    @Test
    public void testBudgetId() {
        budget.setId(10L);
        assertEquals(10L, budget.getId());
    }

    @Test
    public void testBudgetLimitAmountPrecision() {
        BigDecimal preciseAmount = new BigDecimal("999.99");
        budget.setLimitAmount(preciseAmount);
        assertEquals(preciseAmount, budget.getLimitAmount());
    }

    @Test
    public void testBudgetAllFieldsCombined() {
        budget.setId(1L);
        budget.setCategory("Groceries");
        budget.setLimitAmount(new BigDecimal("250.50"));
        budget.setMonth(3);
        budget.setYear(2025);
        
        assertNotNull(budget);
        assertEquals(1L, budget.getId());
        assertEquals("Groceries", budget.getCategory());
        assertEquals(new BigDecimal("250.50"), budget.getLimitAmount());
        assertEquals(3, budget.getMonth());
        assertEquals(2025, budget.getYear());
    }
}
