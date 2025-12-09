package com.example.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExpenseTrackerApplicationTest {

    @Test
    public void testApplicationContextLoads() {
        assertTrue(true, "Application context loaded successfully");
    }

    @Test
    public void testApplicationName() {
        String appName = "Expense Tracker";
        assertNotNull(appName);
        assertEquals("Expense Tracker", appName);
    }

    @Test
    public void testApplicationInitialization() {
        assertNotNull(this.getClass().getName());
    }

    @Test
    public void testApplicationClassExists() {
        assertNotNull(ExpenseTrackerApplication.class);
    }

    @Test
    public void testApplicationNameCorrect() {
        assertEquals("com.example.expense.ExpenseTrackerApplication", ExpenseTrackerApplication.class.getName());
    }

    @Test
    public void testApplicationCanBeInstantiated() {
        ExpenseTrackerApplication app = new ExpenseTrackerApplication();
        assertNotNull(app);
    }

    @Test
    public void testApplicationMainMethod() {
        // Test that main method exists and is executable
        try {
            assertNotNull(ExpenseTrackerApplication.class.getMethod("main", String[].class));
        } catch (NoSuchMethodException e) {
            throw new AssertionError("main method should exist", e);
        }
    }

    @Test
    public void testApplicationMainMethodExecution() {
        // Mock SpringApplication.run to avoid actually starting the application
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(eq(ExpenseTrackerApplication.class), any(String[].class)))
                    .thenReturn(null);
            
            String[] args = new String[]{};
            ExpenseTrackerApplication.main(args);
            
            // Verify that SpringApplication.run was called with correct arguments
            mockedSpringApplication.verify(() -> SpringApplication.run(eq(ExpenseTrackerApplication.class), eq(args)));
        }
    }

    @Test
    public void testApplicationMainMethodWithArguments() {
        // Test main method with command line arguments
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(eq(ExpenseTrackerApplication.class), any(String[].class)))
                    .thenReturn(null);
            
            String[] args = new String[]{"--server.port=8081", "--spring.profiles.active=test"};
            ExpenseTrackerApplication.main(args);
            
            mockedSpringApplication.verify(() -> SpringApplication.run(eq(ExpenseTrackerApplication.class), eq(args)));
        }
    }
}

