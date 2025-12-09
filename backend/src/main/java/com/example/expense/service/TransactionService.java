package com.example.expense.service;

import com.example.expense.dto.TransactionDto;
import com.example.expense.domain.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    Transaction create(TransactionDto dto);
    List<Transaction> list(LocalDateTime from, LocalDateTime to);
}
