package com.example.expense.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class MonthlyReportTest {
    private MonthlyReport report;

    @BeforeEach
    public void setUp() {
        report = new MonthlyReport();
    }

    @Test
    public void testMonthlyReportCreation() {
        report.setMonth(1);
        report.setYear(2024);
        assertEquals(1, report.getMonth());
        assertEquals(2024, report.getYear());
    }

    @Test
    public void testMonthlyReportWithIncomeAndExpense() {
        report.setMonth(5);
        report.setYear(2024);
        report.setTotalIncome(new BigDecimal("1000.00"));
        report.setTotalExpense(new BigDecimal("500.00"));
        
        assertEquals(new BigDecimal("1000.00"), report.getTotalIncome());
        assertEquals(new BigDecimal("500.00"), report.getTotalExpense());
    }

    @Test
    public void testMonthlyReportNetIncome() {
        report.setTotalIncome(new BigDecimal("2000.00"));
        report.setTotalExpense(new BigDecimal("800.00"));
        
        BigDecimal netIncome = report.getTotalIncome().subtract(report.getTotalExpense());
        assertEquals(new BigDecimal("1200.00"), netIncome);
    }

    @Test
    public void testMonthlyReportWithZeroValues() {
        assertEquals(BigDecimal.ZERO, report.getTotalIncome());
        assertEquals(BigDecimal.ZERO, report.getTotalExpense());
    }

    @Test
    public void testMonthlyReportSummary() {
        report.setSummary("Test summary for January 2024");
        assertEquals("Test summary for January 2024", report.getSummary());
    }

    @Test
    public void testMonthlyReportBuilder() {
        MonthlyReport builtReport = MonthlyReport.builder()
                .id(3L)
                .month(7)
                .year(2024)
                .totalIncome(new BigDecimal("5000.00"))
                .totalExpense(new BigDecimal("2000.00"))
                .summary("July 2024 Report")
                .build();
        
        assertEquals(3L, builtReport.getId());
        assertEquals(7, builtReport.getMonth());
        assertEquals(2024, builtReport.getYear());
        assertEquals(new BigDecimal("5000.00"), builtReport.getTotalIncome());
        assertEquals(new BigDecimal("2000.00"), builtReport.getTotalExpense());
        assertEquals("July 2024 Report", builtReport.getSummary());
    }

    @Test
    public void testMonthlyReportId() {
        report.setId(15L);
        assertEquals(15L, report.getId());
    }

    @Test
    public void testMonthlyReportDefaultValues() {
        assertEquals(BigDecimal.ZERO, report.getTotalIncome());
        assertEquals(BigDecimal.ZERO, report.getTotalExpense());
        assertNull(report.getSummary());
    }

    @Test
    public void testMonthlyReportIncomeExpenseCombinations() {
        report.setTotalIncome(new BigDecimal("3500.00"));
        report.setTotalExpense(new BigDecimal("1200.00"));
        
        assertEquals(new BigDecimal("3500.00"), report.getTotalIncome());
        assertEquals(new BigDecimal("1200.00"), report.getTotalExpense());
        
        BigDecimal net = report.getTotalIncome().subtract(report.getTotalExpense());
        assertEquals(new BigDecimal("2300.00"), net);
    }

    @Test
    public void testMonthlyReportAllFields() {
        report.setId(20L);
        report.setMonth(11);
        report.setYear(2024);
        report.setTotalIncome(new BigDecimal("8000.00"));
        report.setTotalExpense(new BigDecimal("3500.00"));
        report.setSummary("November 2024 Summary");
        
        assertNotNull(report);
        assertEquals(20L, report.getId());
        assertEquals(11, report.getMonth());
        assertEquals(2024, report.getYear());
        assertEquals(new BigDecimal("8000.00"), report.getTotalIncome());
        assertEquals(new BigDecimal("3500.00"), report.getTotalExpense());
        assertEquals("November 2024 Summary", report.getSummary());
    }
}
