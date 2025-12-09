package com.example.expense.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetCurrentReport() throws Exception {
        mockMvc.perform(get("/api/reports/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(5000.0))
                .andExpect(jsonPath("$.totalExpense").value(2350.0))
                .andExpect(jsonPath("$.byCategory.Food").value(450.0))
                .andExpect(jsonPath("$.byCategory.Transport").value(300.0))
                .andExpect(jsonPath("$.byCategory.Entertainment").value(600.0))
                .andExpect(jsonPath("$.byCategory.Utilities").value(1000.0));
    }

    @Test
    public void testGetCurrentMonthReport() throws Exception {
        mockMvc.perform(get("/api/reports/current-month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(5000.0))
                .andExpect(jsonPath("$.totalExpense").value(2350.0));
    }
}
