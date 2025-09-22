package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.LatestSalaryDTO;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalaryLatestServiceTest {

    @Mock
    private SalaryRecordRepository repository;

    private SalaryLatestService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new SalaryLatestService(repository);
    }

    @Test
    public void testGetLatestSalary_NormalCase() {
        when(repository.findMaxYear()).thenReturn(2025);

        SalaryRecord rec1 = new SalaryRecord();
        rec1.setYear(2025);
        rec1.setCompany("CompanyA");
        rec1.setCurrency("USD");
        rec1.setAmount(new BigDecimal("10000"));
        rec1.setSalaryComponent("Fixed CTC");
        rec1.setSalaryType("Fixed");

        SalaryRecord rec2 = new SalaryRecord();
        rec2.setYear(2025);
        rec2.setCompany("CompanyB");
        rec2.setCurrency("INR");
        rec2.setAmount(new BigDecimal("800000"));
        rec2.setSalaryComponent("Fixed CTC");
        rec2.setSalaryType("Fixed");

        when(repository.findByYear(2025)).thenReturn(List.of(rec1, rec2));

        List<LatestSalaryDTO> results = service.getLatestSalary("USD");

        assertEquals(2, results.size());

        LatestSalaryDTO dto1 = results.get(0);
        assertEquals("CompanyA", dto1.company());
        assertEquals(new BigDecimal("10000"), dto1.originalAmount());
        assertEquals(new BigDecimal("10000.0000"), dto1.convertedAmount());

        LatestSalaryDTO dto2 = results.get(1);
        BigDecimal expectedConverted = new BigDecimal("800000").multiply(new BigDecimal("0.012"))
                .divide(BigDecimal.ONE, 4, BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedConverted, dto2.convertedAmount());
    }

    @Test
    public void testGetLatestSalary_NoData() {
        when(repository.findMaxYear()).thenReturn(null);

        List<LatestSalaryDTO> results = service.getLatestSalary("USD");

        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetLatestSalary_UnknownCurrencyDefaultsToOne() {
        when(repository.findMaxYear()).thenReturn(2024);

        SalaryRecord rec = new SalaryRecord();
        rec.setYear(2024);
        rec.setCompany("CompanyC");
        rec.setCurrency("XYZ"); // unknown currency
        rec.setAmount(new BigDecimal("5000"));
        rec.setSalaryComponent("Fixed");
        rec.setSalaryType("Fixed");

        when(repository.findByYear(2024)).thenReturn(List.of(rec));

        List<LatestSalaryDTO> results = service.getLatestSalary("USD");

        assertEquals(1, results.size());
        assertEquals(new BigDecimal("5000.0000"), results.get(0).convertedAmount());
    }

    @Test
    public void testGetLatestSalary_BaseCurrencyOtherThanUSD() {
        when(repository.findMaxYear()).thenReturn(2023);

        SalaryRecord rec = new SalaryRecord();
        rec.setYear(2023);
        rec.setCompany("CompanyD");
        rec.setCurrency("USD");
        rec.setAmount(new BigDecimal("15000"));

        when(repository.findByYear(2023)).thenReturn(List.of(rec));

        List<LatestSalaryDTO> results = service.getLatestSalary("AED");

        BigDecimal baseRate = new BigDecimal("0.27");
        BigDecimal expectedConverted = new BigDecimal("15000").multiply(BigDecimal.ONE)
                .divide(baseRate, 4, BigDecimal.ROUND_HALF_UP);

        assertEquals(expectedConverted, results.get(0).convertedAmount());
    }

}
