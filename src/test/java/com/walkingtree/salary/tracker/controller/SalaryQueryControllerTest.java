package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.dto.SalaryDTO;
import com.walkingtree.salary.tracker.service.SalaryQueryService;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = true)
@SpringBootTest
public class SalaryQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SalaryQueryService queryService;

    @Test
    public void testGetSalariesByYear_Success() throws Exception {
        SalaryDTO dto1 = new SalaryDTO("Fixed CTC", "Fixed CTC", "USD", new BigDecimal("50000"), new BigDecimal("50000"));
        SalaryDTO dto2 = new SalaryDTO("Bonus", "Variable", "USD", new BigDecimal("10000"), new BigDecimal("10000"));

        when(queryService.getSalariesByYear(2024)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/salaries/2024")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salaryComponent").value("Fixed CTC"))
                .andExpect(jsonPath("$[1].salaryComponent").value("Bonus"));
    }

    @Test
    public void testGetSalariesByYear_EmptyResult() throws Exception {
        when(queryService.getSalariesByYear(anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/api/salaries/2025")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testGetSalariesByYear_InvalidYear() throws Exception {
        // Passing invalid year like negative number - handled by path variable validation or custom exception handler
        mockMvc.perform(get("/api/salaries/-1"))
                .andExpect(status().isBadRequest());
    }
}
