package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.dto.YearlySalaryTrend;
import com.walkingtree.salary.tracker.service.SalaryTrendService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = true)
@SpringBootTest
public class SalaryTrendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SalaryTrendService trendService;

    @Test
    public void testGetSalaryTrend_DefaultCurrency() throws Exception {
        YearlySalaryTrend trend1 = new YearlySalaryTrend(2022, new BigDecimal("100000"));
        YearlySalaryTrend trend2 = new YearlySalaryTrend(2023, new BigDecimal("120000"));

        when(trendService.getSalaryGrowthTrend("USD")).thenReturn(List.of(trend1, trend2));

        mockMvc.perform(get("/api/salaries/trend").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].year").value(2022))
                .andExpect(jsonPath("$[0].totalAmountInBaseCurrency").value(100000))
                .andExpect(jsonPath("$[1].year").value(2023))
                .andExpect(jsonPath("$[1].totalAmountInBaseCurrency").value(120000));
    }

    @Test
    public void testGetSalaryTrend_SpecifiedCurrency() throws Exception {
        YearlySalaryTrend trend = new YearlySalaryTrend(2024, new BigDecimal("110000"));
        when(trendService.getSalaryGrowthTrend("EUR")).thenReturn(List.of(trend));

        mockMvc.perform(get("/api/salaries/trend?baseCurrency=EUR").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].year").value(2024))
                .andExpect(jsonPath("$[0].totalAmountInBaseCurrency").value(110000));
    }

    @Test
    public void testGetSalaryTrend_EmptyResult() throws Exception {
        when(trendService.getSalaryGrowthTrend(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/api/salaries/trend").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
