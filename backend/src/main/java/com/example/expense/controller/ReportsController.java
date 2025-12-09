package com.example.expense.controller;

import com.example.expense.domain.Transaction;
import com.example.expense.repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class ReportsController {

    private final TransactionRepository transactionRepository;

    public ReportsController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // GET: Dashboard summary with total income, total expense, and breakdown by category
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String type) {
        
        List<Transaction> transactions = transactionRepository.findAll();
        
        // Filter by account if specified
        if (accountId != null) {
            transactions = transactions.stream()
                    .filter(t -> t.getAccount() != null && t.getAccount().getId().equals(accountId))
                    .collect(Collectors.toList());
        }
        
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        Map<String, BigDecimal> byCategory = new HashMap<>();

        // Separate income and expenses
        List<Transaction> incomeTransactions = transactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
        
        List<Transaction> expenseTransactions = transactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .collect(Collectors.toList());

        // Calculate totals based on type filter
        if ("income".equalsIgnoreCase(type)) {
            for (Transaction tx : incomeTransactions) {
                totalIncome = totalIncome.add(tx.getAmount());
                // Group by category for pie chart
                String category = tx.getCategory() != null ? tx.getCategory() : "Uncategorized";
                byCategory.put(category, byCategory.getOrDefault(category, BigDecimal.ZERO).add(tx.getAmount()));
            }
        } else if ("expense".equalsIgnoreCase(type)) {
            for (Transaction tx : expenseTransactions) {
                totalExpense = totalExpense.add(tx.getAmount().abs());
                // Group by category for pie chart
                String category = tx.getCategory() != null ? tx.getCategory() : "Uncategorized";
                byCategory.put(category, byCategory.getOrDefault(category, BigDecimal.ZERO).add(tx.getAmount().abs()));
            }
        } else {
            // No type filter - show both income and expense totals
            for (Transaction tx : incomeTransactions) {
                totalIncome = totalIncome.add(tx.getAmount());
            }
            for (Transaction tx : expenseTransactions) {
                totalExpense = totalExpense.add(tx.getAmount().abs());
                // Group by category for pie chart
                String category = tx.getCategory() != null ? tx.getCategory() : "Uncategorized";
                byCategory.put(category, byCategory.getOrDefault(category, BigDecimal.ZERO).add(tx.getAmount().abs()));
            }
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("byCategory", byCategory);

        return ResponseEntity.ok(summary);
    }
}
