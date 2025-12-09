package com.example.expense.service;

import com.example.expense.domain.Budget;

import java.util.List;
import java.util.Optional;

public interface BudgetService {
    Budget createBudget(Budget budget);
    Optional<Budget> getBudgetById(Long id);
    List<Budget> getAllBudgets();
    List<Budget> getBudgetsByMonthYear(int month, int year);
    Budget updateBudget(Long id, Budget budget);
    void deleteBudget(Long id);
}
