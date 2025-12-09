package com.example.expense.repository;

import com.example.expense.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByCategoryAndMonthAndYear(String category, int month, int year);
    List<Budget> findByMonthAndYear(int month, int year);
}
