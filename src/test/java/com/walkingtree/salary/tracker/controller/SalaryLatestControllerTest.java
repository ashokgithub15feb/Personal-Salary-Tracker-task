package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.dto.LatestSalaryDTO;
import com.walkingtree.salary.tracker.service.SalaryLatestService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalaryLatestController.class)
public class SalaryLatestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SalaryLatestService latestService;

    @Test
    public void testGetLatestSalary_DefaultCurrency() throws Exception {
        LatestSalaryDTO dto = new LatestSalaryDTO(2025, "CompanyA", "AED", new BigDecimal("10000"), new BigDecimal("10000"), "Fixed CTC", "Fixed CTC");

        when(latestService.getLatestSalary("AED")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/salaries/latest").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].company").value("CompanyA"))
                .andExpect(jsonPath("$[0].currency").value("AED"))
                .andExpect(jsonPath("$[0].year").value(2025))
                .andExpect(jsonPath("$[0].originalAmount").value(10000))
                .andExpect(jsonPath("$[0].convertedAmount").value(10000));
    }

    @Test
    public void testGetLatestSalary_SpecifiedCurrency() throws Exception {
        LatestSalaryDTO dto = new LatestSalaryDTO(2024, "CompanyB", "USD", new BigDecimal("12000"), new BigDecimal("14500"), "Fixed CTC", "Fixed CTC");

        when(latestService.getLatestSalary("USD")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/salaries/latest?baseCurrency=USD").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].company").value("CompanyB"))
                .andExpect(jsonPath("$[0].currency").value("USD"));
    }

    @Test
    public void testGetLatestSalary_EmptyResult() throws Exception {
        when(latestService.getLatestSalary(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/api/salaries/latest").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
