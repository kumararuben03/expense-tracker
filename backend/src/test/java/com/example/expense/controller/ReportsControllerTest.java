package com.example.expense.controller;

import com.example.expense.domain.Transaction;
import com.example.expense.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReportsControllerTest {
    private MockMvc mvc;

    @Mock
    private TransactionRepository transactionRepository;

    private ReportsController reportsController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        reportsController = new ReportsController(transactionRepository);
        this.mvc = MockMvcBuilders.standaloneSetup(reportsController).build();
    }

    @Test
    void getSummary_emptyTransactions_returnsZeroTotals() throws Exception {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/reports/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(0.0))
                .andExpect(jsonPath("$.totalExpense").value(0.0))
                .andExpect(jsonPath("$.byCategory", aMapWithSize(0)));
    }

    @Test
    void getSummary_onlyIncomeTransactions_calculatesTotalIncome() throws Exception {
        Transaction t1 = new Transaction();
        t1.setId(1L);
        t1.setDescription("Salary");
        t1.setAmount(new BigDecimal("5000.00"));
        t1.setCategory("Income");
        t1.setTimestamp(LocalDateTime.now());

        Transaction t2 = new Transaction();
        t2.setId(2L);
        t2.setDescription("Bonus");
        t2.setAmount(new BigDecimal("1000.00"));
        t2.setCategory("Income");
        t2.setTimestamp(LocalDateTime.now());

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        mvc.perform(get("/api/reports/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(6000.0))
                .andExpect(jsonPath("$.totalExpense").value(0.0));
    }

    @Test
    void getSummary_onlyExpenseTransactions_calculatesTotalExpense() throws Exception {
        Transaction t1 = new Transaction();
        t1.setId(1L);
        t1.setDescription("Groceries");
        t1.setAmount(new BigDecimal("-150.00"));
        t1.setCategory("Food");
        t1.setTimestamp(LocalDateTime.now());

        Transaction t2 = new Transaction();
        t2.setId(2L);
        t2.setDescription("Gas");
        t2.setAmount(new BigDecimal("-50.00"));
        t2.setCategory("Transport");
        t2.setTimestamp(LocalDateTime.now());

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        mvc.perform(get("/api/reports/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(0.0))
                .andExpect(jsonPath("$.totalExpense").value(200.0));
    }

    @Test
    void getSummary_mixedTransactions_calculatesCorrectTotals() throws Exception {
        Transaction income = new Transaction();
        income.setId(1L);
        income.setDescription("Salary");
        income.setAmount(new BigDecimal("5000.00"));
        income.setCategory("Income");
        income.setTimestamp(LocalDateTime.now());

        Transaction expense1 = new Transaction();
        expense1.setId(2L);
        expense1.setDescription("Utilities");
        expense1.setAmount(new BigDecimal("-500.00"));
        expense1.setCategory("Utilities");
        expense1.setTimestamp(LocalDateTime.now());

        Transaction expense2 = new Transaction();
        expense2.setId(3L);
        expense2.setDescription("Food");
        expense2.setAmount(new BigDecimal("-200.00"));
        expense2.setCategory("Food");
        expense2.setTimestamp(LocalDateTime.now());

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(income, expense1, expense2));

        mvc.perform(get("/api/reports/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(5000.0))
                .andExpect(jsonPath("$.totalExpense").value(700.0));
    }

    @Test
    void getSummary_categorizeExpenses_groupsByCategory() throws Exception {
        Transaction util1 = new Transaction();
        util1.setId(1L);
        util1.setDescription("Electric");
        util1.setAmount(new BigDecimal("-300.00"));
        util1.setCategory("Utilities");
        util1.setTimestamp(LocalDateTime.now());

        Transaction util2 = new Transaction();
        util2.setId(2L);
        util2.setDescription("Water");
        util2.setAmount(new BigDecimal("-100.00"));
        util2.setCategory("Utilities");
        util2.setTimestamp(LocalDateTime.now());

        Transaction food = new Transaction();
        food.setId(3L);
        food.setDescription("Groceries");
        food.setAmount(new BigDecimal("-150.00"));
        food.setCategory("Food");
        food.setTimestamp(LocalDateTime.now());

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(util1, util2, food));

        mvc.perform(get("/api/reports/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.byCategory.Utilities").value(400.0))
                .andExpect(jsonPath("$.byCategory.Food").value(150.0))
                .andExpect(jsonPath("$.byCategory", aMapWithSize(2)));
    }

    @Test
    void getSummary_multipleCategoriesWithDuplicates_sumsCategoryAmounts() throws Exception {
        Transaction e1 = new Transaction();
        e1.setId(1L);
        e1.setAmount(new BigDecimal("-100.00"));
        e1.setCategory("Entertainment");

        Transaction e2 = new Transaction();
        e2.setId(2L);
        e2.setAmount(new BigDecimal("-200.00"));
        e2.setCategory("Entertainment");

        Transaction e3 = new Transaction();
        e3.setId(3L);
        e3.setAmount(new BigDecimal("-300.00"));
        e3.setCategory("Entertainment");

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(e1, e2, e3));

        mvc.perform(get("/api/reports/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.byCategory.Entertainment").value(600.0))
                .andExpect(jsonPath("$.totalExpense").value(600.0));
    }

    @Test
    void getSummary_transactionWithoutCategory_usesUncategorized() throws Exception {
        Transaction uncategorized = new Transaction();
        uncategorized.setId(1L);
        uncategorized.setDescription("Unknown expense");
        uncategorized.setAmount(new BigDecimal("-50.00"));
        uncategorized.setCategory(null);
        uncategorized.setTimestamp(LocalDateTime.now());

        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(uncategorized));

        mvc.perform(get("/api/reports/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.byCategory.Uncategorized").value(50.0))
                .andExpect(jsonPath("$.totalExpense").value(50.0));
    }

    @Test
    void getSummary_largeAmounts_handlesBigDecimalPrecision() throws Exception {
        Transaction large = new Transaction();
        large.setId(1L);
        large.setAmount(new BigDecimal("999999.99"));
        large.setCategory("Income");

        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(large));

        mvc.perform(get("/api/reports/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(999999.99));
    }

    @Test
    void getSummary_returnsMapStructure() throws Exception {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/reports/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").exists())
                .andExpect(jsonPath("$.totalExpense").exists())
                .andExpect(jsonPath("$.byCategory").exists());
    }

    @Test
    void getSummary_filterByTypeIncome_returnsOnlyIncome() throws Exception {
        Transaction income1 = new Transaction();
        income1.setId(1L);
        income1.setAmount(new BigDecimal("3000.00"));
        income1.setCategory("Income");

        Transaction expense = new Transaction();
        expense.setId(2L);
        expense.setAmount(new BigDecimal("-500.00"));
        expense.setCategory("Food");

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(income1, expense));

        mvc.perform(get("/api/reports/summary")
                .param("type", "income")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(3000.0))
                .andExpect(jsonPath("$.totalExpense").value(0.0))
                .andExpect(jsonPath("$.byCategory", aMapWithSize(0)));
    }

    @Test
    void getSummary_filterByTypeExpense_returnsOnlyExpense() throws Exception {
        Transaction income = new Transaction();
        income.setId(1L);
        income.setAmount(new BigDecimal("3000.00"));
        income.setCategory("Income");

        Transaction expense1 = new Transaction();
        expense1.setId(2L);
        expense1.setAmount(new BigDecimal("-500.00"));
        expense1.setCategory("Food");

        Transaction expense2 = new Transaction();
        expense2.setId(3L);
        expense2.setAmount(new BigDecimal("-200.00"));
        expense2.setCategory("Utilities");

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(income, expense1, expense2));

        mvc.perform(get("/api/reports/summary")
                .param("type", "expense")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(0.0))
                .andExpect(jsonPath("$.totalExpense").value(700.0))
                .andExpect(jsonPath("$.byCategory.Food").value(500.0))
                .andExpect(jsonPath("$.byCategory.Utilities").value(200.0));
    }

    @Test
    void getSummary_filterByAccountId_returnsOnlyAccountTransactions() throws Exception {
        com.example.expense.domain.Account account1 = new com.example.expense.domain.Account();
        account1.setId(1L);
        account1.setName("Checking");

        com.example.expense.domain.Account account2 = new com.example.expense.domain.Account();
        account2.setId(2L);
        account2.setName("Savings");

        Transaction t1 = new Transaction();
        t1.setId(1L);
        t1.setAmount(new BigDecimal("1000.00"));
        t1.setCategory("Income");
        t1.setAccount(account1);

        Transaction t2 = new Transaction();
        t2.setId(2L);
        t2.setAmount(new BigDecimal("-200.00"));
        t2.setCategory("Food");
        t2.setAccount(account2);

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        mvc.perform(get("/api/reports/summary")
                .param("accountId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(1000.0))
                .andExpect(jsonPath("$.totalExpense").value(0.0));
    }

    @Test
    void getSummary_filterByAccountIdWithNoMatches_returnsZeroTotals() throws Exception {
        com.example.expense.domain.Account account1 = new com.example.expense.domain.Account();
        account1.setId(1L);
        account1.setName("Checking");

        Transaction t1 = new Transaction();
        t1.setId(1L);
        t1.setAmount(new BigDecimal("1000.00"));
        t1.setCategory("Income");
        t1.setAccount(account1);

        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(t1));

        mvc.perform(get("/api/reports/summary")
                .param("accountId", "999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(0.0))
                .andExpect(jsonPath("$.totalExpense").value(0.0));
    }

    @Test
    void getSummary_filterByTypeExpenseWithUncategorized_includesUncategorizedInByCategory() throws Exception {
        Transaction expense = new Transaction();
        expense.setId(1L);
        expense.setAmount(new BigDecimal("-100.00"));
        expense.setCategory(null);

        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(expense));

        mvc.perform(get("/api/reports/summary")
                .param("type", "expense")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalExpense").value(100.0))
                .andExpect(jsonPath("$.byCategory.Uncategorized").value(100.0));
    }

    @Test
    void getSummary_accountWithNullValue_isSkipped() throws Exception {
        Transaction t1 = new Transaction();
        t1.setId(1L);
        t1.setAmount(new BigDecimal("1000.00"));
        t1.setCategory("Income");
        t1.setAccount(null);

        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(t1));

        mvc.perform(get("/api/reports/summary")
                .param("accountId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(0.0))
                .andExpect(jsonPath("$.totalExpense").value(0.0));
    }

    @Test
    void getSummary_combinedFilters_accountIdAndTypeExpense() throws Exception {
        com.example.expense.domain.Account account1 = new com.example.expense.domain.Account();
        account1.setId(1L);
        account1.setName("Checking");

        Transaction income = new Transaction();
        income.setId(1L);
        income.setAmount(new BigDecimal("5000.00"));
        income.setCategory("Income");
        income.setAccount(account1);

        Transaction expense1 = new Transaction();
        expense1.setId(2L);
        expense1.setAmount(new BigDecimal("-200.00"));
        expense1.setCategory("Food");
        expense1.setAccount(account1);

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(income, expense1));

        mvc.perform(get("/api/reports/summary")
                .param("accountId", "1")
                .param("type", "expense")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(0.0))
                .andExpect(jsonPath("$.totalExpense").value(200.0))
                .andExpect(jsonPath("$.byCategory.Food").value(200.0));
    }
}
