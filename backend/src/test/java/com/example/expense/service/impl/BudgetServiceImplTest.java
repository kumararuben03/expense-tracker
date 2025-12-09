package com.example.expense.service.impl;

import com.example.expense.domain.Budget;
import com.example.expense.repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceImplTest {
    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private Budget budget;

    @BeforeEach
    void setUp() {
        budget = Budget.builder()
                .id(1L)
                .category("Food")
                .limitAmount(BigDecimal.valueOf(500))
                .month(12)
                .year(2025)
                .build();
    }

    @Test
    void createBudget_withValidData_returnsBudget() {
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget result = budgetService.createBudget(budget);

        assertNotNull(result);
        assertEquals("Food", result.getCategory());
        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void getBudgetById_withExistingId_returnsBudget() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));

        Optional<Budget> result = budgetService.getBudgetById(1L);

        assertTrue(result.isPresent());
        assertEquals("Food", result.get().getCategory());
    }

    @Test
    void getBudgetById_withNonExistingId_returnsEmpty() {
        when(budgetRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Budget> result = budgetService.getBudgetById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllBudgets_returnsListOfBudgets() {
        when(budgetRepository.findAll()).thenReturn(List.of(budget));

        List<Budget> result = budgetService.getAllBudgets();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getBudgetsByMonthYear_returnsMatchingBudgets() {
        when(budgetRepository.findByMonthAndYear(12, 2025)).thenReturn(List.of(budget));

        List<Budget> result = budgetService.getBudgetsByMonthYear(12, 2025);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateBudget_withValidId_updatesBudget() {
        Budget updated = Budget.builder()
                .id(1L)
                .category("Entertainment")
                .limitAmount(BigDecimal.valueOf(800))
                .month(12)
                .year(2025)
                .build();

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(updated);

        Budget result = budgetService.updateBudget(1L, updated);

        assertNotNull(result);
        assertEquals("Entertainment", result.getCategory());
    }

    @Test
    void deleteBudget_callsRepository() {
        budgetService.deleteBudget(1L);

        verify(budgetRepository).deleteById(1L);
    }

    @Test
    void createBudget_multipleBudgets_eachCallsSave() {
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        budgetService.createBudget(budget);
        budgetService.createBudget(budget);
        budgetService.createBudget(budget);

        verify(budgetRepository, times(3)).save(any(Budget.class));
    }

    @Test
    void getBudgetsByMonthYear_emptyResult_returnsEmptyList() {
        when(budgetRepository.findByMonthAndYear(1, 2026)).thenReturn(List.of());

        List<Budget> result = budgetService.getBudgetsByMonthYear(1, 2026);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void updateBudget_nonExistingId_throwsException() {
        when(budgetRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> budgetService.updateBudget(999L, budget));
    }
}
