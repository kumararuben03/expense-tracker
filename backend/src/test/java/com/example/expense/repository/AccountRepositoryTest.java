package com.example.expense.repository;

import com.example.expense.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;

public class AccountRepositoryTest {
    @Mock
    private AccountRepository accountRepository;
    
    private Account account;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setId(1L);
        account.setName("Test Account");
        account.setCurrency("USD");
        account.setBalance(new BigDecimal("1000.00"));
    }

    @Test
    public void testSaveAccount() {
        when(accountRepository.save(account)).thenReturn(account);
        Account result = accountRepository.save(account);
        
        assertNotNull(result);
        assertEquals("Test Account", result.getName());
        verify(accountRepository).save(account);
    }

    @Test
    public void testFindById() {
        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.of(account));
        var result = accountRepository.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(account, result.get());
    }

    @Test
    public void testDeleteAccount() {
        doNothing().when(accountRepository).delete(any(Account.class));
        accountRepository.delete(account);
        verify(accountRepository).delete(any(Account.class));
    }

    @Test
    public void testAccountWithCurrencyChange() {
        account.setCurrency("EUR");
        assertEquals("EUR", account.getCurrency());
    }
}

