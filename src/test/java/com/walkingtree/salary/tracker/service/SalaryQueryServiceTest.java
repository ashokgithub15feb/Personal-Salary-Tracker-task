package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.SalaryDTO;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalaryQueryServiceTest {

    @Mock
    private SalaryRecordRepository repository;

    private SalaryQueryService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new SalaryQueryService(repository);
    }

    @Test
    public void testGetSalariesByYear_NormalData() {
        SalaryRecord rec1 = new SalaryRecord();
        rec1.setSalaryComponent("Fixed CTC");
        rec1.setSalaryType("Fixed");
        rec1.setCurrency("USD");
        rec1.setAmount(new BigDecimal("10000"));

        SalaryRecord rec2 = new SalaryRecord();
        rec2.setSalaryComponent("Bonus");
        rec2.setSalaryType("Variable");
        rec2.setCurrency("INR");
        rec2.setAmount(new BigDecimal("50000"));

        when(repository.findByYear(2024)).thenReturn(List.of(rec1, rec2));

        List<SalaryDTO> results = service.getSalariesByYear(2024);

        assertEquals(2, results.size());

        SalaryDTO dto1 = results.get(0);
        assertEquals("Fixed CTC", dto1.getSalaryComponent());
        assertEquals("Fixed", dto1.getSalaryType());
        assertEquals(new BigDecimal("10000"), dto1.getAmount());
        assertEquals(new BigDecimal("10000"), dto1.getAmountInBaseCurrenc()); // USD default

        SalaryDTO dto2 = results.get(1);
        assertEquals(new BigDecimal("50000"), dto2.getAmount());
        assertEquals(new BigDecimal("600.0000"), dto2.getAmountInBaseCurrenc()); // 50000 * 0.012
    }

    @Test
    public void testGetSalariesByYear_EmptyList() {
        when(repository.findByYear(2025)).thenReturn(List.of());

        List<SalaryDTO> results = service.getSalariesByYear(2025);

        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetSalariesByYear_UnknownCurrency() {
        SalaryRecord rec = new SalaryRecord();
        rec.setSalaryComponent("Fixed CTC");
        rec.setSalaryType("Fixed");
        rec.setCurrency("XYZ"); // Unknown currency
        rec.setAmount(new BigDecimal("12000"));

        when(repository.findByYear(2023)).thenReturn(List.of(rec));

        List<SalaryDTO> results = service.getSalariesByYear(2023);

        assertEquals(1, results.size());
        assertEquals(new BigDecimal("12000"), results.get(0).getAmountInBaseCurrenc()); // fallback rate 1
    }
}

