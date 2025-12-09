package com.example.expense.repository;

import com.example.expense.domain.Budget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Collections;

public class BudgetRepositoryTest {
    @Mock
    private BudgetRepository budgetRepository;
    
    private Budget budget;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        budget = new Budget();
        budget.setId(1L);
        budget.setCategory("Food");
        budget.setLimitAmount(new BigDecimal("500.00"));
        budget.setMonth(1);
        budget.setYear(2024);
    }

    @Test
    public void testFindById() {
        when(budgetRepository.findById(1L)).thenReturn(java.util.Optional.of(budget));
        var result = budgetRepository.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(budget, result.get());
    }

    @Test
    public void testFindAll() {
        when(budgetRepository.findAll()).thenReturn(Collections.singletonList(budget));
        var results = budgetRepository.findAll();
        
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    public void testSaveBudget() {
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
        Budget result = budgetRepository.save(budget);
        
        assertNotNull(result);
        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    public void testDeleteBudget() {
        doNothing().when(budgetRepository).delete(any(Budget.class));
        budgetRepository.delete(budget);
        verify(budgetRepository).delete(any(Budget.class));
    }

    @Test
    public void testBudgetWithDifferentCategory() {
        budget.setCategory("Entertainment");
        assertEquals("Entertainment", budget.getCategory());
    }
}
