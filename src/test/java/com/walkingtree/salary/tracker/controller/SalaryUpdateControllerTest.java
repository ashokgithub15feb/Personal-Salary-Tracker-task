package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.service.SalaryUpdateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = true)
@SpringBootTest
public class SalaryUpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SalaryUpdateService updateService;

    @Test
    public void testApplyHike_Success() throws Exception {
        when(updateService.updateSalaryHike(anyInt(), any(BigDecimal.class))).thenReturn(5);

        mockMvc.perform(put("/api/salaries/2024/hike")
                        .param("percent", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated 5 salary records with 10% hike"));
    }

    @Test
    public void testApplyHike_NoRecordsFound() throws Exception {
        when(updateService.updateSalaryHike(anyInt(), any(BigDecimal.class))).thenReturn(0);

        mockMvc.perform(put("/api/salaries/2023/hike")
                        .param("percent", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No records found for year 2023"));
    }

    @Test
    public void testApplyHike_MissingPercentParam() throws Exception {
        mockMvc.perform(put("/api/salaries/2024/hike")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testApplyHike_InvalidPercentParam() throws Exception {
        mockMvc.perform(put("/api/salaries/2024/hike")
                        .param("percent", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
