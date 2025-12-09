package com.example.expense.service;

import com.example.expense.domain.Account;
import com.example.expense.domain.Transaction;
import com.example.expense.dto.TransactionDto;
import com.example.expense.repository.AccountRepository;
import com.example.expense.repository.TransactionRepository;
import com.example.expense.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    private TransactionRepository txRepo;
    private AccountRepository accountRepo;
    private TransactionServiceImpl service;

    @BeforeEach
    void setup() {
        txRepo = mock(TransactionRepository.class);
        accountRepo = mock(AccountRepository.class);
        service = new TransactionServiceImpl(txRepo, accountRepo);
    }

    @Test
    void create_updatesAccountBalanceWhenAccountPresent() {
        Account a = Account.builder().id(1L).name("Wallet").balance(BigDecimal.valueOf(100)).build();
        when(accountRepo.findById(1L)).thenReturn(Optional.of(a));
        when(txRepo.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        TransactionDto dto = new TransactionDto();
        dto.description = "Coffee";
        dto.amount = BigDecimal.valueOf(-5);
        dto.category = "Food";
        dto.accountId = 1L;

        Transaction saved = service.create(dto);

        verify(txRepo, times(1)).save(any(Transaction.class));
        ArgumentCaptor<Account> cap = ArgumentCaptor.forClass(Account.class);
        verify(accountRepo).save(cap.capture());
        assertEquals(BigDecimal.valueOf(95), cap.getValue().getBalance());
        assertEquals("Coffee", saved.getDescription());
    }
}
