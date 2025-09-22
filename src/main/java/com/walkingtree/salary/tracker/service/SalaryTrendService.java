package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.YearlySalaryTrend;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalaryTrendService {

    private final SalaryRecordRepository repository;

    // Example hard-coded conversion rates
    private static final Map<String, BigDecimal> conversionRates = Map.of(
            "USD", BigDecimal.ONE,
            "INR", new BigDecimal("0.012"),
            "EUR", new BigDecimal("1.1")
    );

    public SalaryTrendService(SalaryRecordRepository repository) {
        this.repository = repository;
    }

    public List<YearlySalaryTrend> getSalaryGrowthTrend(String baseCurrency) {
        BigDecimal baseRate = conversionRates.getOrDefault(baseCurrency.toUpperCase(), BigDecimal.ONE);

        List<SalaryRecord> allRecords = repository.findAll();

        // Group by year and sum amounts converted to baseCurrency
        Map<Integer, BigDecimal> yearToAmount = new HashMap<>();

        for (SalaryRecord record : allRecords) {
            BigDecimal rate = conversionRates.getOrDefault(record.getCurrency(), BigDecimal.ONE);
            BigDecimal amountInBase = record.getAmount().multiply(rate).divide(baseRate, 4, BigDecimal.ROUND_HALF_UP);
            yearToAmount.merge(record.getYear(), amountInBase, BigDecimal::add);
        }

        return yearToAmount.entrySet().stream()
                .map(entry -> new YearlySalaryTrend(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(YearlySalaryTrend::year))
                .collect(Collectors.toList());
    }
}
