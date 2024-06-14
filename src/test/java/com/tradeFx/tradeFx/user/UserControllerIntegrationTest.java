package com.tradeFx.tradeFx.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeFx.tradeFx.trade.Trade;
import com.tradeFx.tradeFx.trade.TradeRepository;
import com.tradeFx.tradeFx.transaction.Transaction;
import com.tradeFx.tradeFx.transaction.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllUsersSuccess() throws Exception {
        // Populate test data
        User user1 = userRepository.save(new User(null, "John Doe", "john.doe@example.com", "0xabcdef123456", true, false, "secret", "password", "1000", "active", "profile.jpg", "user", null, null, LocalDateTime.now(), LocalDateTime.now()));
        User user2 = userRepository.save(new User(null, "Jane Smith", "jane.smith@example.com", "0x123456789abc", true, false, "secret", "password", "1500", "active", "profile.jpg", "user", null, null, LocalDateTime.now(), LocalDateTime.now()));

        // Send GET request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Deserialize response
        List<User> users = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        // Assertions
        assertEquals(2, users.size());
        // Add more assertions as needed
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        // Mock data
        User userToCreate = new User();
        userToCreate.setName("John Doe");
        userToCreate.setEmail("john.doe@example.com");
        userToCreate.setEthAddress("0xabcdef123456");
        userToCreate.setEmailVerified(true);
        userToCreate.setAccountVerified(false);
        userToCreate.setSecret("secret");
        userToCreate.setPassword("password");
        userToCreate.setBalance("1000");
        userToCreate.setStatus("active");
        userToCreate.setProfileImg("profile.jpg");
        userToCreate.setUserRole("user");

        // Send POST request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn();

        // Deserialize response
        User createdUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        // Assertions
        assertNotNull(createdUser.getId());
        assertEquals(userToCreate.getName(), createdUser.getName());
        assertEquals(userToCreate.getEmail(), createdUser.getEmail());
        // Add more assertions as needed
    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {
        // Populate test data
        User user = userRepository.save(new User(null, "John Doe", "john.doe@example.com", "0xabcdef123456", true, false, "secret", "password", "1000", "active", "profile.jpg", "user", null, null, LocalDateTime.now(), LocalDateTime.now()));

        // Send GET request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Deserialize response
        User retrievedUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        // Assertions
        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getName(), retrievedUser.getName());
        // Add more assertions as needed
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        // Populate test data
        User existingUser = userRepository.save(new User(null, "John Doe", "john.doe@example.com", "0xabcdef123456", true, false, "secret", "password", "1000", "active", "profile.jpg", "user", null, null, LocalDateTime.now(), LocalDateTime.now()));

        // Populate trades and transactions
        List<Trade> trades = new ArrayList<>();
        // Populate trades as needed
        List<Transaction> transactions = new ArrayList<>();
        // Populate transactions as needed

        existingUser.setTrades(trades);
        existingUser.setTransactions(transactions);

        // Mock data
        User updatedUser = new User(existingUser.getId(), "Jane Smith", "jane.smith@example.com", "0x123456789abc", true, true, "new-secret", "new-password", "1500", "inactive", "new-profile.jpg", "admin", trades, transactions, LocalDateTime.now(), LocalDateTime.now());

        // Send PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());

        // Retrieve updated user from database
        User modifiedUser = userRepository.findById(existingUser.getId()).orElse(null);

        // Assertions
        assertNotNull(modifiedUser);
        assertEquals(updatedUser.getName(), modifiedUser.getName());
        assertEquals(updatedUser.getEmail(), modifiedUser.getEmail());
        // Add more assertions as needed
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        // Populate test data
        User existingUser = userRepository.save(new User(null, "John Doe", "john.doe@example.com", "0xabcdef123456", true, false, "secret", "password", "1000", "active", "profile.jpg", "user", null, null, LocalDateTime.now(), LocalDateTime.now()));

        // Send DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", existingUser.getId()))
                .andExpect(status().isOk());

        // Verify that the user was deleted
        assertFalse(userRepository.existsById(existingUser.getId()));
    }

}

