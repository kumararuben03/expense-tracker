package com.example.expense.scheduler;

import com.example.expense.service.ReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportScheduler {
    private final ReportService reportService;

    public ReportScheduler(ReportService reportService) {
        this.reportService = reportService;
    }

    // runs monthly (can be configured in application.yml)
    @Scheduled(cron = "0 0 1 1 * ?")
    public void generateMonthlyReport() {
        reportService.generateMonthlyReportForPreviousMonth();
    }
}
