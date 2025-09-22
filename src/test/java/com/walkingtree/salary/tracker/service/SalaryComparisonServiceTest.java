package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.CompanySalaryComparison;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalaryComparisonServiceTest {

    @Mock
    private SalaryRecordRepository repository;

    private SalaryComparisonService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new SalaryComparisonService(repository);
    }

    @Test
    public void testCompareAverageSalaries_NormalData() {
        SalaryRecord rec1 = new SalaryRecord();
        rec1.setCompany("CompanyA");
        rec1.setCurrency("USD");
        rec1.setAmount(new BigDecimal("1000"));

        SalaryRecord rec2 = new SalaryRecord();
        rec2.setCompany("CompanyA");
        rec2.setCurrency("USD");
        rec2.setAmount(new BigDecimal("3000"));

        SalaryRecord rec3 = new SalaryRecord();
        rec3.setCompany("CompanyB");
        rec3.setCurrency("INR");
        rec3.setAmount(new BigDecimal("100000"));

        when(repository.findAll()).thenReturn(List.of(rec1, rec2, rec3));

        List<CompanySalaryComparison> result = service.compareAverageSalaries("USD");

        assertEquals(2, result.size());

        CompanySalaryComparison compA = result.stream()
                .filter(c -> c.company().equals("CompanyA"))
                .findFirst()
                .orElseThrow();

        BigDecimal expectedAvgA = new BigDecimal("2000.0000"); // (1000 + 3000) / 2
        BigDecimal expectedConvertedA = expectedAvgA.multiply(BigDecimal.ONE).divide(BigDecimal.ONE, 4, BigDecimal.ROUND_HALF_UP);

        assertEquals(expectedAvgA, compA.avgSalaryOriginal());
        assertEquals("USD", compA.originalCurrency());
        assertEquals(expectedConvertedA, compA.avgSalaryInBaseCurrency());

        CompanySalaryComparison compB = result.stream()
                .filter(c -> c.company().equals("CompanyB"))
                .findFirst()
                .orElseThrow();

        BigDecimal expectedAvgB = new BigDecimal("100000.0000");
        BigDecimal expectedConvertedB = expectedAvgB.multiply(new BigDecimal("0.012")).divide(BigDecimal.ONE, 4, BigDecimal.ROUND_HALF_UP);

        assertEquals(expectedAvgB, compB.avgSalaryOriginal());
        assertEquals("INR", compB.originalCurrency());
        assertEquals(expectedConvertedB, compB.avgSalaryInBaseCurrency());
    }

    @Test
    public void testCompareAverageSalaries_EmptyData() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<CompanySalaryComparison> result = service.compareAverageSalaries("USD");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCompareAverageSalaries_UnknownCurrencyDefaults() {
        SalaryRecord rec = new SalaryRecord();
        rec.setCompany("CompanyX");
        rec.setCurrency("XYZ"); // unknown currency
        rec.setAmount(new BigDecimal("5000"));

        when(repository.findAll()).thenReturn(List.of(rec));

        List<CompanySalaryComparison> result = service.compareAverageSalaries("USD");
        assertEquals(1, result.size());
        assertEquals("XYZ", result.get(0).originalCurrency());
        assertEquals(new BigDecimal("5000.0000"), result.get(0).avgSalaryInBaseCurrency());
    }

    @Test
    public void testCompareAverageSalaries_BaseCurrencyOtherThanUSD() {
        SalaryRecord rec = new SalaryRecord();
        rec.setCompany("CompanyA");
        rec.setCurrency("USD");
        rec.setAmount(new BigDecimal("100"));

        when(repository.findAll()).thenReturn(List.of(rec));

        List<CompanySalaryComparison> result = service.compareAverageSalaries("EUR");
        assertEquals(1, result.size());

        BigDecimal baseRate = new BigDecimal("1.1");
        BigDecimal expectedConverted = new BigDecimal("100.0000").multiply(BigDecimal.ONE).divide(baseRate, 4, BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedConverted, result.get(0).avgSalaryInBaseCurrency());
    }
}

