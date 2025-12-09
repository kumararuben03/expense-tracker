package com.example.expense.service.impl;

import com.example.expense.domain.Account;
import com.example.expense.domain.Transaction;
import com.example.expense.dto.TransactionDto;
import com.example.expense.repository.TransactionRepository;
import com.example.expense.repository.AccountRepository;
import com.example.expense.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository txRepo;
    private final AccountRepository accountRepo;

    public TransactionServiceImpl(TransactionRepository txRepo, AccountRepository accountRepo) {
        this.txRepo = txRepo;
        this.accountRepo = accountRepo;
    }

    @Override
    @Transactional
    public Transaction create(TransactionDto dto) {
        Transaction tx = Transaction.builder()
                .description(dto.description)
                .amount(dto.amount)
                .category(dto.category)
                .timestamp(dto.timestamp == null ? LocalDateTime.now() : dto.timestamp)
                .build();

        if (dto.accountId != null) {
            Optional<Account> a = accountRepo.findById(dto.accountId);
            a.ifPresent(tx::setAccount);
        }

        Transaction saved = txRepo.save(tx);
        // update account balance
        if (saved.getAccount() != null) {
            Account acc = saved.getAccount();
            acc.setBalance(acc.getBalance().add(saved.getAmount()));
            accountRepo.save(acc);
        }
        return saved;
    }

    @Override
    public List<Transaction> list(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null) {
            return txRepo.findByTimestampBetween(from, to);
        }
        return txRepo.findAll();
    }
}
