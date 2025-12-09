package com.example.expense.service.impl;

import com.example.expense.domain.Account;
import com.example.expense.repository.AccountRepository;
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
public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
                .name("My Account")
                .currency("USD")
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    @Test
    void createAccount_withValidData_returnsAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.createAccount(account);

        assertNotNull(result);
        assertEquals("My Account", result.getName());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void getAccountById_withExistingId_returnsAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.getAccountById(1L);

        assertTrue(result.isPresent());
        assertEquals("My Account", result.get().getName());
    }

    @Test
    void getAccountById_withNonExistingId_returnsEmpty() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Account> result = accountService.getAccountById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllAccounts_returnsListOfAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<Account> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateAccount_withValidId_updatesAccount() {
        Account updated = Account.builder()
                .id(1L)
                .name("Updated Account")
                .currency("EUR")
                .balance(BigDecimal.valueOf(2000))
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(updated);

        Account result = accountService.updateAccount(1L, updated);

        assertNotNull(result);
        assertEquals("Updated Account", result.getName());
    }

    @Test
    void deleteAccount_callsRepository() {
        accountService.deleteAccount(1L);

        verify(accountRepository).deleteById(1L);
    }

    @Test
    void updateBalance_withValidId_addsAmount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.updateBalance(1L, BigDecimal.valueOf(500));

        assertNotNull(result);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void updateBalance_withNegativeAmount_decreasesBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.updateBalance(1L, BigDecimal.valueOf(-300));

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_multipleAccounts_eachCallsSave() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.createAccount(account);
        accountService.createAccount(account);
        accountService.createAccount(account);

        verify(accountRepository, times(3)).save(any(Account.class));
    }
}
