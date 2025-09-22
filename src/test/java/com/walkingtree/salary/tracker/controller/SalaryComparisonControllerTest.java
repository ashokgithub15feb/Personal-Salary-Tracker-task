package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.dto.CompanySalaryComparison;
import com.walkingtree.salary.tracker.service.SalaryComparisonService;
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

@WebMvcTest(SalaryComparisonController.class)
public class SalaryComparisonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SalaryComparisonService comparisonService;

    @Test
    public void testCompareSalaries_DefaultCurrency() throws Exception {
        CompanySalaryComparison comp1 = new CompanySalaryComparison("CompanyA", new BigDecimal("50000"), "USD", new BigDecimal("50000"));
        CompanySalaryComparison comp2 = new CompanySalaryComparison("CompanyB", new BigDecimal("40000"), "INR", new BigDecimal("480"));

        when(comparisonService.compareAverageSalaries("USD")).thenReturn(List.of(comp1, comp2));

        mockMvc.perform(get("/api/salaries/compare").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].company").value("CompanyA"))
                .andExpect(jsonPath("$[0].avgSalaryOriginal").value(50000))
                .andExpect(jsonPath("$[1].company").value("CompanyB"))
                .andExpect(jsonPath("$[1].avgSalaryInBaseCurrency").value(480));
    }

    @Test
    public void testCompareSalaries_SpecifiedCurrency() throws Exception {
        CompanySalaryComparison comp = new CompanySalaryComparison("CompanyC", new BigDecimal("120000"), "EUR", new BigDecimal("132000"));

        when(comparisonService.compareAverageSalaries("EUR")).thenReturn(List.of(comp));

        mockMvc.perform(get("/api/salaries/compare?baseCurrency=EUR").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].company").value("CompanyC"))
                .andExpect(jsonPath("$[0].avgSalaryOriginal").value(120000));
    }

    @Test
    public void testCompareSalaries_EmptyResult() throws Exception {
        when(comparisonService.compareAverageSalaries(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/api/salaries/compare").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
