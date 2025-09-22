package com.walkingtree.salary.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkingtree.salary.tracker.dto.SalarySwitchDTO;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.service.SalarySwitchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalarySwitchController.class)
public class SalarySwitchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SalarySwitchService switchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSwitchCompany_Success() throws Exception {
        SalarySwitchDTO dto = new SalarySwitchDTO();
        dto.setCompany("NewCo");
        dto.setCurrency("USD");
        dto.setYear(2025);
        dto.setSalaryComponent("Fixed CTC");
        dto.setSalaryType("Fixed CTC");
        dto.setAmount(new BigDecimal("120000"));

        SalaryRecord savedRecord = new SalaryRecord();
        savedRecord.setCompany("NewCo");

        when(switchService.addNewSalaryRecord(any(SalarySwitchDTO.class))).thenReturn(savedRecord);

        mockMvc.perform(post("/api/salaries/switch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("New salary record added for company: NewCo"));
    }

    @Test
    public void testSwitchCompany_MissingBody() throws Exception {
        mockMvc.perform(post("/api/salaries/switch")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSwitchCompany_InvalidBody() throws Exception {
        String invalidJson = "{\"year\":\"notAnInt\"}";

        mockMvc.perform(post("/api/salaries/switch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
