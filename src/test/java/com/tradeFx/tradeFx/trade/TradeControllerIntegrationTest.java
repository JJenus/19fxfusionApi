package com.tradeFx.tradeFx.trade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeFx.tradeFx.user.User;
import com.tradeFx.tradeFx.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        tradeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testPlaceTradeSuccess() throws Exception {
        // Mock data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user = userRepository.save(user);

        Trade trade = new Trade();
        trade.setUser(user);
        trade.setLots(1.0);
        trade.setTradeType(TradeType.LONG);
        trade.setEntryPrice(100.0);
        trade.setStopLossPrice(95.0);
        trade.setTakeProfitPrice(110.0);
        trade.setStatus(Status.OPEN);

        // Send POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/trades/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trade)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        // Assertions
        assertEquals(1, tradeRepository.count());
    }

    @Test
    public void testUpdateTradeSuccess() throws Exception {
        // Mock data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user = userRepository.save(user);

        Trade trade = new Trade();
        trade.setUser(user);
        trade.setLots(1.0);
        trade.setTradeType(TradeType.LONG);
        trade.setEntryPrice(100.0);
        trade.setStopLossPrice(95.0);
        trade.setTakeProfitPrice(110.0);
        trade.setStatus(Status.OPEN);
        trade = tradeRepository.save(trade);

        // Update data
        trade.setStopLossPrice(96.0);
        trade.setTakeProfitPrice(111.0);

        // Send PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/trades/{tradeId}", trade.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trade)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stopLossPrice").value(96.0))
                .andExpect(jsonPath("$.takeProfitPrice").value(111.0));

        // Assertions
        Trade updatedTrade = tradeRepository.findById(trade.getId()).orElse(null);
        assertNotNull(updatedTrade);
        assertEquals(96.0, updatedTrade.getStopLossPrice(), 0.01);
        assertEquals(111.0, updatedTrade.getTakeProfitPrice(), 0.01);
    }

    @Test
    public void testCloseTradeSuccess() throws Exception {
        // Mock data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user = userRepository.save(user);

        Trade trade = new Trade();
        trade.setUser(user);
        trade.setLots(1.0);
        trade.setTradeType(TradeType.LONG);
        trade.setEntryPrice(100.0);
        trade.setStopLossPrice(95.0);
        trade.setTakeProfitPrice(110.0);
        trade.setStatus(Status.OPEN);
        trade = tradeRepository.save(trade);

        // Close trade
        double currentPrice = 105.0;

        // Send PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/trades/{tradeId}/close/{currentPrice}", trade.getId(), currentPrice)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Status.CLOSED.toString()))
                .andExpect(jsonPath("$.profitLoss").value(5.0));

        // Assertions
        Trade closedTrade = tradeRepository.findById(trade.getId()).orElse(null);
        assertNotNull(closedTrade);
        assertEquals(Status.CLOSED, closedTrade.getStatus());
        assertEquals(5.0, closedTrade.getProfitLoss(), 0.01);
    }
}
