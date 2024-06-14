package com.tradeFx.tradeFx.historicalData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeFx.tradeFx.HistoricalData.HistoricalData;
import com.tradeFx.tradeFx.HistoricalData.HistoricalDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HistoricalDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoricalDataRepository historicalDataRepository;

    @BeforeEach
    public void setup() {
        historicalDataRepository.deleteAll();
    }

    @Test
    public void testCreateHistoricalDataSuccess() throws Exception {
        HistoricalData historicalData = new HistoricalData();
        historicalData.setTimestamp(LocalDateTime.now());
        historicalData.setOpen(100.0);
        historicalData.setHigh(105.0);
        historicalData.setLow(95.0);
        historicalData.setClose(102.0);
        historicalData.setVolume(1000.0);
        historicalData.setTimeframe("1 min");

        mockMvc.perform(MockMvcRequestBuilders.post("/historical-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(historicalData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testGetAllHistoricalDataSuccess() throws Exception {
        HistoricalData historicalData1 = new HistoricalData();
        historicalData1.setTimestamp(LocalDateTime.now());
        historicalData1.setOpen(100.0);
        historicalData1.setHigh(105.0);
        historicalData1.setLow(95.0);
        historicalData1.setClose(102.0);
        historicalData1.setVolume(1000.0);
        historicalData1.setTimeframe("1 min");

        HistoricalData historicalData2 = new HistoricalData();
        historicalData2.setTimestamp(LocalDateTime.now());
        historicalData2.setOpen(200.0);
        historicalData2.setHigh(205.0);
        historicalData2.setLow(195.0);
        historicalData2.setClose(202.0);
        historicalData2.setVolume(2000.0);
        historicalData2.setTimeframe("5 min");

        historicalDataRepository.saveAll(List.of(historicalData1, historicalData2));

        mockMvc.perform(MockMvcRequestBuilders.get("/historical-data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetHistoricalDataByIdSuccess() throws Exception {
        HistoricalData historicalData = new HistoricalData();
        historicalData.setTimestamp(LocalDateTime.now());
        historicalData.setOpen(100.0);
        historicalData.setHigh(105.0);
        historicalData.setLow(95.0);
        historicalData.setClose(102.0);
        historicalData.setVolume(1000.0);
        historicalData.setTimeframe("1 min");

        historicalData = historicalDataRepository.save(historicalData);

        mockMvc.perform(MockMvcRequestBuilders.get("/historical-data/{id}", historicalData.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(historicalData.getId().intValue())));
    }

    @Test
    public void testUpdateHistoricalDataSuccess() throws Exception {
        HistoricalData historicalData = new HistoricalData();
        historicalData.setTimestamp(LocalDateTime.now());
        historicalData.setOpen(100.0);
        historicalData.setHigh(105.0);
        historicalData.setLow(95.0);
        historicalData.setClose(102.0);
        historicalData.setVolume(1000.0);
        historicalData.setTimeframe("1 min");

        historicalData = historicalDataRepository.save(historicalData);

        HistoricalData updatedData = new HistoricalData();
        updatedData.setTimestamp(LocalDateTime.now());
        updatedData.setOpen(101.0);
        updatedData.setHigh(106.0);
        updatedData.setLow(96.0);
        updatedData.setClose(103.0);
        updatedData.setVolume(1100.0);
        updatedData.setTimeframe("1 min");

        mockMvc.perform(MockMvcRequestBuilders.put("/historical-data/{id}", historicalData.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.open", is(101.0)))
                .andExpect(jsonPath("$.high", is(106.0)))
                .andExpect(jsonPath("$.low", is(96.0)))
                .andExpect(jsonPath("$.close", is(103.0)))
                .andExpect(jsonPath("$.volume", is(1100.0)));
    }

    @Test
    public void testDeleteHistoricalDataSuccess() throws Exception {
        HistoricalData historicalData = new HistoricalData();
        historicalData.setTimestamp(LocalDateTime.now());
        historicalData.setOpen(100.0);
        historicalData.setHigh(105.0);
        historicalData.setLow(95.0);
        historicalData.setClose(102.0);
        historicalData.setVolume(1000.0);
        historicalData.setTimeframe("1 min");

        historicalData = historicalDataRepository.save(historicalData);

        mockMvc.perform(MockMvcRequestBuilders.delete("/historical-data/{id}", historicalData.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, historicalDataRepository.count());
    }
}