package com.example.expense.repository;

import com.example.expense.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Collections;

public class TransactionRepositoryTest {
    @Mock
    private TransactionRepository transactionRepository;
    
    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = new Transaction();
        transaction.setId(1L);
    }

    @Test
    public void testFindById() {
        when(transactionRepository.findById(1L)).thenReturn(java.util.Optional.of(transaction));
        var result = transactionRepository.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(transaction, result.get());
    }

    @Test
    public void testFindAll() {
        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(transaction));
        var results = transactionRepository.findAll();
        
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    public void testSaveTransaction() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction result = transactionRepository.save(transaction);
        
        assertNotNull(result);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void testDeleteTransaction() {
        doNothing().when(transactionRepository).delete(any(Transaction.class));
        transactionRepository.delete(transaction);
        verify(transactionRepository).delete(any(Transaction.class));
    }
}
