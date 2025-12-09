package com.example.expense.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void testHandleThrowable() {
        Exception exception = new Exception("Test error");
        ResponseEntity<ApiError> response = handler.handleThrowable(exception);

        assertNotNull(response);
        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    public void testHandleRuntimeException() {
        RuntimeException exception = new RuntimeException("Runtime error");
        ResponseEntity<ApiError> response = handler.handleThrowable(exception);

        assertNotNull(response);
        assertEquals(500, response.getStatusCode().value());
    }
}
