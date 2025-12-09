package com.example.expense.controller;

import com.example.expense.domain.Budget;
import com.example.expense.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {
    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

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
    void createBudget_withValidData_returnsCreated() {
        when(budgetService.createBudget(any(Budget.class))).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.createBudget(budget);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Food", response.getBody().getCategory());
    }

    @Test
    void getAllBudgets_returnsOk() {
        when(budgetService.getAllBudgets()).thenReturn(List.of(budget));

        ResponseEntity<List<Budget>> response = budgetController.getAllBudgets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getBudgetById_withExistingId_returnsOk() {
        when(budgetService.getBudgetById(1L)).thenReturn(Optional.of(budget));

        ResponseEntity<Budget> response = budgetController.getBudgetById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getBudgetById_withNonExistingId_returnsNotFound() {
        when(budgetService.getBudgetById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Budget> response = budgetController.getBudgetById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getBudgetsByMonthYear_returnsOk() {
        when(budgetService.getBudgetsByMonthYear(12, 2025)).thenReturn(List.of(budget));

        ResponseEntity<List<Budget>> response = budgetController.getBudgetsByMonthYear(12, 2025);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateBudget_withValidId_returnsOk() {
        when(budgetService.updateBudget(1L, budget)).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.updateBudget(1L, budget);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateBudget_withNonExistingId_returnsNotFound() {
        when(budgetService.updateBudget(999L, budget)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<Budget> response = budgetController.updateBudget(999L, budget);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteBudget_returnsNoContent() {
        ResponseEntity<Void> response = budgetController.deleteBudget(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(budgetService).deleteBudget(1L);
    }

    @Test
    void getBudgetsByMonthYear_emptyResult_returnsEmptyList() {
        when(budgetService.getBudgetsByMonthYear(1, 2026)).thenReturn(List.of());

        ResponseEntity<List<Budget>> response = budgetController.getBudgetsByMonthYear(1, 2026);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}
