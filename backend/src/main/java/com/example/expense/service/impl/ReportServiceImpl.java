package com.example.expense.service.impl;

import com.example.expense.domain.MonthlyReport;
import com.example.expense.repository.MonthlyReportRepository;
import com.example.expense.repository.TransactionRepository;
import com.example.expense.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final TransactionRepository txRepo;
    private final MonthlyReportRepository reportRepo;

    public ReportServiceImpl(TransactionRepository txRepo, MonthlyReportRepository reportRepo) {
        this.txRepo = txRepo;
        this.reportRepo = reportRepo;
    }

    @Override
    @Transactional
    public void generateMonthlyReportForPreviousMonth() {
        YearMonth prev = YearMonth.now().minusMonths(1);
        LocalDateTime from = prev.atDay(1).atStartOfDay();
        LocalDateTime to = prev.atEndOfMonth().atTime(23,59,59);

        List<com.example.expense.domain.Transaction> txs = txRepo.findByTimestampBetween(from, to);
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        for (var t : txs) {
            if (t.getAmount().compareTo(BigDecimal.ZERO) >= 0) income = income.add(t.getAmount());
            else expense = expense.add(t.getAmount().abs());
        }

        MonthlyReport r = reportRepo.findByMonthAndYear(prev.getMonthValue(), prev.getYear())
                .orElseGet(() -> MonthlyReport.builder().month(prev.getMonthValue()).year(prev.getYear()).build());
        r.setTotalIncome(income);
        r.setTotalExpense(expense);
        r.setSummary("Auto-generated report");
        reportRepo.save(r);
    }
}
