package com.example.expense.repository;

import com.example.expense.domain.MonthlyReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Collections;

public class MonthlyReportRepositoryTest {
    @Mock
    private MonthlyReportRepository monthlyReportRepository;
    
    private MonthlyReport report;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        report = new MonthlyReport();
        report.setId(1L);
        report.setMonth(1);
        report.setYear(2024);
        report.setTotalIncome(new BigDecimal("5000.00"));
        report.setTotalExpense(new BigDecimal("3000.00"));
    }

    @Test
    public void testFindById() {
        when(monthlyReportRepository.findById(1L)).thenReturn(java.util.Optional.of(report));
        var result = monthlyReportRepository.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(report, result.get());
    }

    @Test
    public void testFindAll() {
        when(monthlyReportRepository.findAll()).thenReturn(Collections.singletonList(report));
        var results = monthlyReportRepository.findAll();
        
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    public void testSaveMonthlyReport() {
        when(monthlyReportRepository.save(any(MonthlyReport.class))).thenReturn(report);
        MonthlyReport result = monthlyReportRepository.save(report);
        
        assertNotNull(result);
        verify(monthlyReportRepository).save(any(MonthlyReport.class));
    }

    @Test
    public void testDeleteMonthlyReport() {
        doNothing().when(monthlyReportRepository).delete(any(MonthlyReport.class));
        monthlyReportRepository.delete(report);
        verify(monthlyReportRepository).delete(any(MonthlyReport.class));
    }

    @Test
    public void testMonthlyReportNetIncome() {
        BigDecimal netIncome = report.getTotalIncome().subtract(report.getTotalExpense());
        assertEquals(new BigDecimal("2000.00"), netIncome);
    }
}
