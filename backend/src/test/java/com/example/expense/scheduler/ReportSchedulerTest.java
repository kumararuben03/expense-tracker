package com.example.expense.scheduler;

import com.example.expense.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportSchedulerTest {

    @Mock
    private ReportService reportService;
    
    private ReportScheduler scheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        scheduler = new ReportScheduler(reportService);
    }

    @Test
    public void testSchedulerCanBeCreated() {
        assertNotNull(scheduler);
    }

    @Test
    public void testGenerateMonthlyReportForPreviousMonthIsCalled() {
        scheduler.generateMonthlyReport();
        verify(reportService, times(1)).generateMonthlyReportForPreviousMonth();
    }

    @Test
    public void testSchedulerWithMockedService() {
        doNothing().when(reportService).generateMonthlyReportForPreviousMonth();
        scheduler.generateMonthlyReport();
        verify(reportService).generateMonthlyReportForPreviousMonth();
    }
}


