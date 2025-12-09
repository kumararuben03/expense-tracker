package com.example.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {
    public Long id;
    public String description;
    public BigDecimal amount;
    public String category;
    public LocalDateTime timestamp;
    public Long accountId;
}
