package com.tradeFx.tradeFx.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeFx.tradeFx.user.User;
import com.tradeFx.tradeFx.user.UserRepository;
import com.tradeFx.tradeFx.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        transactionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateTransactionSuccess() throws Exception {
        // Mock data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setBalance("1000.00");
        user = userRepository.save(user);

        Transaction transactionToCreate = new Transaction();
        transactionToCreate.setAmount("100.00");
        transactionToCreate.setTransactionType(TransactionType.DEPOSIT);
        transactionToCreate.setUser(user);

        // Send POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionToCreate)))
                .andExpect(status().isOk());

        // Assertions
        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());
        assertEquals("100.00", transactions.get(0).getAmount());
        assertEquals(TransactionType.DEPOSIT, transactions.get(0).getTransactionType());
    }

    @Test
    public void testGetAllTransactionsSuccess() throws Exception {
        // Populate test data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setBalance("1000.00");
        user = userRepository.save(user);

        Transaction transaction1 = new Transaction();
        transaction1.setAmount("100.00");
        transaction1.setTransactionType(TransactionType.DEPOSIT);
        transaction1.setUser(user);
        transaction1 = transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount("200.00");
        transaction2.setTransactionType(TransactionType.WITHDRAW);
        transaction2.setUser(user);
        transaction2 = transactionRepository.save(transaction2);

        // Send GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(transaction1.getId().intValue())));
//                .andExpect(jsonPath("$[1].id", equalTo(transaction2.getId().intValue())));
    }

    @Test
    public void testGetTransactionByIdSuccess() throws Exception {
        // Populate test data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setBalance("1000.00");
        user = userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setAmount("100.00");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setUser(user);
        transaction = transactionRepository.save(transaction);

        // Send GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/{id}", transaction.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(transaction.getId().intValue())));
    }

    @Test
    public void testUpdateTransactionSuccess() throws Exception {
        // Populate test data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setBalance("1000.00");
        user = userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setAmount("100.00");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setUser(user);
        transaction = transactionRepository.save(transaction);

        // Mock data for update
        TransactionStatus newStatus = TransactionStatus.COMPLETED;

        // Send PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/transactions/{id}", transaction.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStatus)))
                .andExpect(status().isOk());

        // Retrieve updated transaction from the database
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).orElse(null);

        // Assertions
        assertNotNull(updatedTransaction);
        assertEquals(newStatus, updatedTransaction.getStatus());
    }

    @Test
    public void testDeleteTransactionSuccess() throws Exception {
        // Populate test data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setBalance("1000.00");
        user = userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setAmount("100.00");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setUser(user);
        transaction = transactionRepository.save(transaction);

        // Send DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/{id}", transaction.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assertions
        assertFalse(transactionRepository.existsById(transaction.getId()));
    }

    // Add more test cases to cover edge cases and error scenarios

    @Test
    public void testCreateTransactionInvalidUser() throws Exception {
        // Mock data with invalid user ID
        Transaction transactionToCreate = new Transaction();
        User invalidUser = new User();
        invalidUser.setId(999L); // Invalid user ID
        transactionToCreate.setUser(invalidUser);
        transactionToCreate.setAmount("100.00");
        transactionToCreate.setTransactionType(TransactionType.DEPOSIT);

        // Send POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionToCreate)))
                .andExpect(status().isNotFound()); // Expecting a bad request due to invalid user ID
    }

    @Test
    public void testUpdateTransactionNotFound() throws Exception {
        // Mock data with non-existing transaction ID
        String newStatus = "COMPLETED";

        // Send PUT request with non-existing transaction ID
        mockMvc.perform(MockMvcRequestBuilders.put("/transactions/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStatus)))
                .andExpect(status().isNotFound()); // Expecting a not found error
    }

    @Test
    public void testDeleteTransactionNotFound() throws Exception {
        // Send DELETE request with non-existing transaction ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expecting a not found error
    }

    @Test
    public void testCreateTransactionInsufficientBalance() throws Exception {
        // Populate test data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setBalance("50.00"); // Balance less than transaction amount
        user = userRepository.save(user);

        Transaction transactionToCreate = new Transaction();
        transactionToCreate.setAmount("100.00");
        transactionToCreate.setTransactionType(TransactionType.WITHDRAW);
        transactionToCreate.setUser(user);

        // Send POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionToCreate)))
                .andExpect(status().isBadRequest()); // Expecting a bad request due to insufficient balance
    }
}
