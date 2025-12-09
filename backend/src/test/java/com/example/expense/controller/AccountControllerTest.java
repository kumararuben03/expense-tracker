package com.example.expense.controller;

import com.example.expense.domain.Account;
import com.example.expense.service.AccountService;
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
public class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
                .name("Checking")
                .currency("USD")
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    @Test
    void createAccount_withValidData_returnsCreated() {
        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        ResponseEntity<Account> response = accountController.createAccount(account);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Checking", response.getBody().getName());
    }

    @Test
    void getAllAccounts_returnsOk() {
        when(accountService.getAllAccounts()).thenReturn(List.of(account));

        ResponseEntity<List<Account>> response = accountController.getAllAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAccountById_withExistingId_returnsOk() {
        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account));

        ResponseEntity<Account> response = accountController.getAccountById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAccountById_withNonExistingId_returnsNotFound() {
        when(accountService.getAccountById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Account> response = accountController.getAccountById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateAccount_withValidId_returnsOk() {
        when(accountService.updateAccount(1L, account)).thenReturn(account);

        ResponseEntity<Account> response = accountController.updateAccount(1L, account);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateAccount_withNonExistingId_returnsNotFound() {
        when(accountService.updateAccount(999L, account)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<Account> response = accountController.updateAccount(999L, account);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteAccount_returnsNoContent() {
        ResponseEntity<Void> response = accountController.deleteAccount(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(accountService).deleteAccount(1L);
    }

    @Test
    void updateBalance_withValidId_returnsOk() {
        when(accountService.updateBalance(1L, BigDecimal.valueOf(500))).thenReturn(account);

        ResponseEntity<Account> response = accountController.updateBalance(1L, BigDecimal.valueOf(500));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateBalance_withNonExistingId_returnsNotFound() {
        when(accountService.updateBalance(999L, BigDecimal.valueOf(500)))
                .thenThrow(new RuntimeException("Not found"));

        ResponseEntity<Account> response = accountController.updateBalance(999L, BigDecimal.valueOf(500));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
