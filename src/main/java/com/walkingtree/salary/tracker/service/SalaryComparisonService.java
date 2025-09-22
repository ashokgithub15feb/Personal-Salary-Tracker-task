package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.CompanySalaryComparison;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalaryComparisonService {

    private final SalaryRecordRepository repository;

    private static final Map<String, BigDecimal> conversionRates = Map.of(
            "USD", BigDecimal.ONE,
            "INR", new BigDecimal("0.012"),
            "AED", new BigDecimal("0.27"),
            "EUR", new BigDecimal("1.1")
    );

    public SalaryComparisonService(SalaryRecordRepository repository) {
        this.repository = repository;
    }

    public List<CompanySalaryComparison> compareAverageSalaries(String baseCurrency) {
        BigDecimal baseRate = conversionRates.getOrDefault(baseCurrency.toUpperCase(), BigDecimal.ONE);

        // Group by company and currency, calculate average amount
        Map<String, List<SalaryRecord>> grouped = repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(SalaryRecord::getCompany));

        List<CompanySalaryComparison> comparisons = new ArrayList<>();

        for (Map.Entry<String, List<SalaryRecord>> entry : grouped.entrySet()) {
            String company = entry.getKey();
            List<SalaryRecord> records = entry.getValue();

            // Assuming all salaries for a company have same currency or take first record's currency
            String originalCurrency = records.get(0).getCurrency();
            BigDecimal avgOriginal = records.stream()
                    .map(SalaryRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(records.size()), 4, BigDecimal.ROUND_HALF_UP);

            BigDecimal rate = conversionRates.getOrDefault(originalCurrency, BigDecimal.ONE);
            BigDecimal avgConverted = avgOriginal.multiply(rate).divide(baseRate, 4, BigDecimal.ROUND_HALF_UP);

            comparisons.add(new CompanySalaryComparison(company, avgOriginal, originalCurrency, avgConverted));
        }

        return comparisons;
    }
}
