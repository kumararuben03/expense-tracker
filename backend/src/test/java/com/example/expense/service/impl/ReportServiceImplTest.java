package com.example.expense.service.impl;

import com.example.expense.domain.MonthlyReport;
import com.example.expense.domain.Transaction;
import com.example.expense.repository.MonthlyReportRepository;
import com.example.expense.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

public class ReportServiceImplTest {
    @Mock
    private TransactionRepository txRepo;
    
    @Mock
    private MonthlyReportRepository reportRepo;
    
    private ReportServiceImpl reportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportServiceImpl(txRepo, reportRepo);
    }

    @Test
    public void testReportServiceInitialization() {
        assertNotNull(reportService);
    }

    @Test
    public void testGenerateMonthlyReportForPreviousMonth() {
        when(txRepo.findAll()).thenReturn(Collections.emptyList());
        when(reportRepo.save(any(MonthlyReport.class))).thenReturn(new MonthlyReport());
        
        reportService.generateMonthlyReportForPreviousMonth();
        
        verify(reportRepo, times(1)).save(any(MonthlyReport.class));
    }

    @Test
    public void testReportServiceWithTransactions() {
        Transaction tx = new Transaction();
        tx.setAmount(new BigDecimal("100.00"));
        tx.setTimestamp(LocalDateTime.now());
        
        when(txRepo.findAll()).thenReturn(Collections.singletonList(tx));
        when(reportRepo.save(any(MonthlyReport.class))).thenReturn(new MonthlyReport());
        
        reportService.generateMonthlyReportForPreviousMonth();
        
        verify(reportRepo).save(any(MonthlyReport.class));
    }

    @Test
    public void testReportGenerationMultipleTimes() {
        when(txRepo.findAll()).thenReturn(Collections.emptyList());
        when(reportRepo.save(any(MonthlyReport.class))).thenReturn(MonthlyReport.builder().build());
        
        reportService.generateMonthlyReportForPreviousMonth();
        reportService.generateMonthlyReportForPreviousMonth();
        
        verify(reportRepo, times(2)).save(any(MonthlyReport.class));
    }

    @Test
    public void testGenerateReportWithPositiveTransactions() {
        Transaction positiveTx = Transaction.builder()
                .amount(new BigDecimal("500.00"))
                .timestamp(LocalDateTime.now())
                .build();
        
        when(txRepo.findByTimestampBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(positiveTx));
        when(reportRepo.findByMonthAndYear(anyInt(), anyInt())).thenReturn(java.util.Optional.empty());
        when(reportRepo.save(any(MonthlyReport.class))).thenReturn(MonthlyReport.builder().build());
        
        reportService.generateMonthlyReportForPreviousMonth();
        
        verify(reportRepo).save(any(MonthlyReport.class));
    }

    @Test
    public void testGenerateReportWithNegativeTransactions() {
        Transaction negativeTx = Transaction.builder()
                .amount(new BigDecimal("-200.00"))
                .timestamp(LocalDateTime.now())
                .build();
        
        when(txRepo.findByTimestampBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(negativeTx));
        when(reportRepo.findByMonthAndYear(anyInt(), anyInt())).thenReturn(java.util.Optional.empty());
        when(reportRepo.save(any(MonthlyReport.class))).thenReturn(MonthlyReport.builder().build());
        
        reportService.generateMonthlyReportForPreviousMonth();
        
        verify(reportRepo).save(any(MonthlyReport.class));
    }

    @Test
    public void testGenerateReportWithMixedTransactions() {
        Transaction positiveTx = Transaction.builder()
                .amount(new BigDecimal("1000.00"))
                .timestamp(LocalDateTime.now())
                .build();
        
        Transaction negativeTx = Transaction.builder()
                .amount(new BigDecimal("-300.00"))
                .timestamp(LocalDateTime.now())
                .build();
        
        when(txRepo.findByTimestampBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(java.util.Arrays.asList(positiveTx, negativeTx));
        when(reportRepo.findByMonthAndYear(anyInt(), anyInt())).thenReturn(java.util.Optional.empty());
        when(reportRepo.save(any(MonthlyReport.class))).thenReturn(MonthlyReport.builder().build());
        
        reportService.generateMonthlyReportForPreviousMonth();
        
        verify(reportRepo).save(any(MonthlyReport.class));
    }

    @Test
    public void testGenerateReportUpdatesExisting() {
        MonthlyReport existingReport = MonthlyReport.builder()
                .month(11)
                .year(2024)
                .build();
        
        when(txRepo.findByTimestampBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(reportRepo.findByMonthAndYear(anyInt(), anyInt())).thenReturn(java.util.Optional.of(existingReport));
        when(reportRepo.save(any(MonthlyReport.class))).thenReturn(existingReport);
        
        reportService.generateMonthlyReportForPreviousMonth();
        
        verify(reportRepo).save(any(MonthlyReport.class));
    }
}

