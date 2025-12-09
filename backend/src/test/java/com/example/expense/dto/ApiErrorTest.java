package com.example.expense.dto;

import com.example.expense.exception.ApiError;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiErrorTest {

    @Test
    public void testApiErrorCreation() {
        ApiError error = new ApiError(400, "Bad Request");
        assertEquals(400, error.getStatus());
        assertEquals("Bad Request", error.getMessage());
    }

    @Test
    public void testApiErrorSettersAndGetters() {
        ApiError error = new ApiError();
        error.setStatus(500);
        error.setMessage("Internal Server Error");
        
        assertEquals(500, error.getStatus());
        assertEquals("Internal Server Error", error.getMessage());
    }

    @Test
    public void testApiErrorWithTimestamp() {
        ApiError error = new ApiError(404, "Not Found");
        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getMessage());
    }
}
