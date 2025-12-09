package com.example.expense.controller;

import com.example.expense.domain.Transaction;
import com.example.expense.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest {
    private MockMvc mvc;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        TransactionController controller = new TransactionController(transactionRepository);
        this.mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void create_withValidData_returnsOk() throws Exception {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        mvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Test\",\"amount\":10}"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void list_withNoFilter_returnsOk() throws Exception {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/transactions"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void list_withMultipleTransactions_returnsAll() throws Exception {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(new Transaction(), new Transaction()));

        mvc.perform(get("/api/transactions"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getById_withExistingId_returnsOk() throws Exception {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(new Transaction()));

        mvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void getById_withNonExistingId_returns404() throws Exception {
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/transactions/999"))
                .andExpect(status().isNotFound());

        verify(transactionRepository, times(1)).findById(999L);
    }

    @Test
    void create_withMultipleFields_savesSuccessfully() throws Exception {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        mvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Full transaction\",\"amount\":100.50,\"category\":\"Test\"}"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void update_existingTransaction_returnsOk() throws Exception {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(new Transaction()));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        mvc.perform(put("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Updated\",\"amount\":75.00}"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void update_nonExistingTransaction_returns404() throws Exception {
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        mvc.perform(put("/api/transactions/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Test\",\"amount\":10}"))
                .andExpect(status().isNotFound());

        verify(transactionRepository, times(1)).findById(999L);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void update_withPartialFields_stillUpdates() throws Exception {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(new Transaction()));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        mvc.perform(put("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":50.00}"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void delete_existingTransaction_returns204() throws Exception {
        when(transactionRepository.existsById(1L)).thenReturn(true);

        mvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isNoContent());

        verify(transactionRepository, times(1)).existsById(1L);
        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_nonExistingTransaction_returns404() throws Exception {
        when(transactionRepository.existsById(999L)).thenReturn(false);

        mvc.perform(delete("/api/transactions/999"))
                .andExpect(status().isNotFound());

        verify(transactionRepository, times(1)).existsById(999L);
        verify(transactionRepository, never()).deleteById(999L);
    }

    @Test
    void create_withNegativeAmount_saves() throws Exception {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        mvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Expense\",\"amount\":-50.00}"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void create_withZeroAmount_saves() throws Exception {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        mvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":0}"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void delete_multipleDeletions_callsRepositoryEachTime() throws Exception {
        when(transactionRepository.existsById(any())).thenReturn(true);

        mvc.perform(delete("/api/transactions/1")).andExpect(status().isNoContent());
        mvc.perform(delete("/api/transactions/2")).andExpect(status().isNoContent());

        verify(transactionRepository, times(2)).deleteById(any());
    }

    @Test
    void create_multipleRequests_eachCallsSave() throws Exception {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        for (int i = 0; i < 3; i++) {
            mvc.perform(post("/api/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"description\":\"T" + i + "\",\"amount\":" + (10 * (i + 1)) + "}"))
                    .andExpect(status().isOk());
        }

        verify(transactionRepository, times(3)).save(any(Transaction.class));
    }

    @Test
    void getById_multipleRequests_callsFindByIdEachTime() throws Exception {
        when(transactionRepository.findById(any())).thenReturn(Optional.of(new Transaction()));

        for (int i = 1; i <= 3; i++) {
            mvc.perform(get("/api/transactions/" + i)).andExpect(status().isOk());
        }

        verify(transactionRepository, times(3)).findById(any());
    }

    @Test
    void create_withLargeAmount_saves() throws Exception {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        mvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Large\",\"amount\":999999.99}"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void update_multipleFields_updatesAll() throws Exception {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(new Transaction()));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        mvc.perform(put("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"New Desc\",\"amount\":123.45,\"category\":\"NewCat\"}"))
                .andExpect(status().isOk());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}

