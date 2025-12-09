package com.example.expense.service.impl;

import com.example.expense.domain.Transaction;
import com.example.expense.dto.TransactionDto;
import com.example.expense.repository.TransactionRepository;
import com.example.expense.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAllTransactions() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();
        List<Transaction> transactions = Arrays.asList(t1, t2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.list(null, null);

        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void testCreateTransaction() {
        TransactionDto dto = new TransactionDto();
        dto.description = "Test";
        dto.amount = new BigDecimal("100.00");
        dto.category = "Food";

        Transaction transaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.create(dto);

        assertNotNull(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testListTransactionsByDateRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        
        Transaction t1 = new Transaction();
        List<Transaction> transactions = Arrays.asList(t1);

        when(transactionRepository.findByTimestampBetween(from, to)).thenReturn(transactions);

        List<Transaction> result = transactionService.list(from, to);

        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findByTimestampBetween(from, to);
    }
}
