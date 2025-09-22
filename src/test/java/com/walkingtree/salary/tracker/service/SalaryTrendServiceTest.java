package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.YearlySalaryTrend;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalaryTrendServiceTest {

    @Mock
    private SalaryRecordRepository repository;

    private SalaryTrendService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new SalaryTrendService(repository);
    }

    @Test
    public void testGetSalaryGrowthTrend_NormalData() {
        SalaryRecord rec1 = new SalaryRecord();
        rec1.setYear(2022);
        rec1.setCurrency("USD");
        rec1.setAmount(new BigDecimal("10000"));

        SalaryRecord rec2 = new SalaryRecord();
        rec2.setYear(2023);
        rec2.setCurrency("INR");
        rec2.setAmount(new BigDecimal("800000"));

        when(repository.findAll()).thenReturn(List.of(rec1, rec2));

        List<YearlySalaryTrend> trends = service.getSalaryGrowthTrend("USD");

        assertEquals(2, trends.size());

        YearlySalaryTrend trend2022 = trends.get(0);
        assertEquals(2022, trend2022.year());
        assertEquals(new BigDecimal("10000.0000"), trend2022.totalAmountInBaseCurrency());

        YearlySalaryTrend trend2023 = trends.get(1);
        BigDecimal expected2023 = new BigDecimal("800000").multiply(new BigDecimal("0.012"))
                .divide(BigDecimal.ONE, 4, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected2023, trend2023.totalAmountInBaseCurrency());
    }

    @Test
    public void testGetSalaryGrowthTrend_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<YearlySalaryTrend> trends = service.getSalaryGrowthTrend("USD");
        assertTrue(trends.isEmpty());
    }

    @Test
    public void testGetSalaryGrowthTrend_UnknownCurrency() {
        SalaryRecord rec = new SalaryRecord();
        rec.setYear(2024);
        rec.setCurrency("XYZ"); // Unknown currency
        rec.setAmount(new BigDecimal("5000"));

        when(repository.findAll()).thenReturn(List.of(rec));

        List<YearlySalaryTrend> trends = service.getSalaryGrowthTrend("USD");
        assertEquals(1, trends.size());
        assertEquals(new BigDecimal("5000.0000"), trends.get(0).totalAmountInBaseCurrency());
    }

    @Test
    public void testGetSalaryGrowthTrend_BaseCurrencyOtherThanUSD() {
        SalaryRecord rec = new SalaryRecord();
        rec.setYear(2025);
        rec.setCurrency("USD");
        rec.setAmount(new BigDecimal("15000"));

        when(repository.findAll()).thenReturn(List.of(rec));

        List<YearlySalaryTrend> trends = service.getSalaryGrowthTrend("EUR");

        BigDecimal baseRate = new BigDecimal("1.1");
        BigDecimal expectedConverted = new BigDecimal("15000.0000").multiply(BigDecimal.ONE)
                .divide(baseRate, 4, BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedConverted, trends.get(0).totalAmountInBaseCurrency());
    }
}

