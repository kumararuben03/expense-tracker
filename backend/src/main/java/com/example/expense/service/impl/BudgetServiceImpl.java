package com.example.expense.service.impl;

import com.example.expense.domain.Budget;
import com.example.expense.repository.BudgetRepository;
import com.example.expense.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;

    @Override
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

    @Override
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    public List<Budget> getBudgetsByMonthYear(int month, int year) {
        return budgetRepository.findByMonthAndYear(month, year);
    }

    @Override
    public Budget updateBudget(Long id, Budget budget) {
        return budgetRepository.findById(id)
                .map(existing -> {
                    existing.setCategory(budget.getCategory());
                    existing.setLimitAmount(budget.getLimitAmount());
                    existing.setMonth(budget.getMonth());
                    existing.setYear(budget.getYear());
                    return budgetRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));
    }

    @Override
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
