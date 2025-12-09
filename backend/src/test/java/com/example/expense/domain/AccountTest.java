package com.example.expense.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountTest {

    @Test
    public void testAccountCreation() {
        Account account = new Account();
        account.setId(1L);
        account.setName("Checking");
        account.setBalance(BigDecimal.valueOf(1000.00));
        
        assertEquals(1L, account.getId());
        assertEquals("Checking", account.getName());
        assertEquals(BigDecimal.valueOf(1000.00), account.getBalance());
    }

    @Test
    public void testAccountSettersAndGetters() {
        Account account = new Account();
        account.setName("Savings");
        account.setBalance(BigDecimal.valueOf(5000.00));
        
        assertNotNull(account);
        assertEquals("Savings", account.getName());
        assertEquals(BigDecimal.valueOf(5000.00), account.getBalance());
    }

    @Test
    public void testAccountWithNullBalance() {
        Account account = new Account();
        account.setName("Investment");
        account.setBalance(null);
        
        assertEquals("Investment", account.getName());
        assertNull(account.getBalance());
    }

    @Test
    public void testAccountIdAssignment() {
        Account account1 = new Account();
        Account account2 = new Account();
        
        account1.setId(1L);
        account2.setId(2L);
        
        assertNotEquals(account1.getId(), account2.getId());
    }

    @Test
    public void testAccountBuilderWithAllFields() {
        List<Transaction> transactions = new ArrayList<>();
        List<Budget> budgets = new ArrayList<>();
        
        Account account = Account.builder()
                .id(1L)
                .name("Premium Checking")
                .currency("USD")
                .balance(BigDecimal.valueOf(10000.00))
                .transactions(transactions)
                .budgets(budgets)
                .build();
        
        assertEquals(1L, account.getId());
        assertEquals("Premium Checking", account.getName());
        assertEquals("USD", account.getCurrency());
        assertEquals(BigDecimal.valueOf(10000.00), account.getBalance());
        assertNotNull(account.getTransactions());
        assertNotNull(account.getBudgets());
    }

    @Test
    public void testAccountBuilderWithDefaultCurrency() {
        Account account = Account.builder()
                .id(2L)
                .name("European Account")
                .build();
        
        assertEquals("USD", account.getCurrency());
        assertEquals(2L, account.getId());
        assertEquals("European Account", account.getName());
    }

    @Test
    public void testAccountBuilderWithDefaultBalance() {
        Account account = Account.builder()
                .id(3L)
                .name("New Account")
                .build();
        
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    public void testAccountBuilderWithCustomCurrency() {
        Account account = Account.builder()
                .id(4L)
                .name("EUR Account")
                .currency("EUR")
                .build();
        
        assertEquals("EUR", account.getCurrency());
    }

    @Test
    public void testAccountCurrencySetterGetter() {
        Account account = new Account();
        account.setName("GBP Account");
        account.setCurrency("GBP");
        account.setBalance(BigDecimal.valueOf(2000.00));
        
        assertEquals("GBP", account.getCurrency());
        assertEquals("GBP Account", account.getName());
    }

    @Test
    public void testAccountWithZeroBalance() {
        Account account = new Account();
        account.setId(5L);
        account.setName("Empty Account");
        account.setBalance(BigDecimal.ZERO);
        
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    public void testAccountWithNegativeBalance() {
        Account account = new Account();
        account.setId(6L);
        account.setName("Overdrawn");
        account.setBalance(BigDecimal.valueOf(-500.00));
        
        assertEquals(BigDecimal.valueOf(-500.00), account.getBalance());
    }

    @Test
    public void testAccountWithLargeBalance() {
        Account account = new Account();
        account.setId(7L);
        account.setName("Investment");
        account.setBalance(BigDecimal.valueOf(1000000.99));
        
        assertEquals(BigDecimal.valueOf(1000000.99), account.getBalance());
    }

    @Test
    public void testAccountTransactionsRelationship() {
        Account account = new Account();
        List<Transaction> transactions = new ArrayList<>();
        
        account.setTransactions(transactions);
        
        assertNotNull(account.getTransactions());
        assertTrue(account.getTransactions().isEmpty());
    }

    @Test
    public void testAccountBudgetsRelationship() {
        Account account = new Account();
        List<Budget> budgets = new ArrayList<>();
        
        account.setBudgets(budgets);
        
        assertNotNull(account.getBudgets());
        assertTrue(account.getBudgets().isEmpty());
    }

    @Test
    public void testAccountNoArgsConstructor() {
        Account account = new Account();
        
        assertNull(account.getId());
        assertNull(account.getName());
        assertEquals("USD", account.getCurrency());
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    public void testAccountAllArgsConstructor() {
        List<Transaction> transactions = new ArrayList<>();
        List<Budget> budgets = new ArrayList<>();
        
        Account account = new Account(
                8L,
                "Full Constructor Account",
                "JPY",
                BigDecimal.valueOf(50000.00),
                transactions,
                budgets
        );
        
        assertEquals(8L, account.getId());
        assertEquals("Full Constructor Account", account.getName());
        assertEquals("JPY", account.getCurrency());
        assertEquals(BigDecimal.valueOf(50000.00), account.getBalance());
        assertEquals(transactions, account.getTransactions());
        assertEquals(budgets, account.getBudgets());
    }

    @Test
    public void testAccountEquality() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setName("Test");
        
        Account account2 = new Account();
        account2.setId(1L);
        account2.setName("Test");
        
        // Objects should be different instances but may have same data
        assertNotSame(account1, account2);
    }

    @Test
    public void testAccountNameUpdate() {
        Account account = new Account();
        account.setName("Original Name");
        
        assertEquals("Original Name", account.getName());
        
        account.setName("Updated Name");
        
        assertEquals("Updated Name", account.getName());
    }

    @Test
    public void testAccountMultipleCurrencies() {
        Account usdAccount = Account.builder()
                .id(1L)
                .name("USD Account")
                .currency("USD")
                .build();
        
        Account eurAccount = Account.builder()
                .id(2L)
                .name("EUR Account")
                .currency("EUR")
                .build();
        
        Account gbpAccount = Account.builder()
                .id(3L)
                .name("GBP Account")
                .currency("GBP")
                .build();
        
        assertEquals("USD", usdAccount.getCurrency());
        assertEquals("EUR", eurAccount.getCurrency());
        assertEquals("GBP", gbpAccount.getCurrency());
    }

    @Test
    public void testAccountWithDecimalBalance() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(1234.56));
        
        assertEquals(BigDecimal.valueOf(1234.56), account.getBalance());
    }

    @Test
    public void testAccountWithHighPrecisionDecimal() {
        Account account = new Account();
        account.setBalance(new BigDecimal("99999.99"));
        
        assertEquals(new BigDecimal("99999.99"), account.getBalance());
    }
}
