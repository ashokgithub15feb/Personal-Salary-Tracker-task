package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.SalaryDTO;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalaryQueryService {

    private final SalaryRecordRepository repository;

    // Simple hardcoded currency conversion for demo (real app use service)
    private static final Map<String, BigDecimal> conversionRates = Map.of(
            "USD", BigDecimal.ONE,
            "INR", new BigDecimal("0.012"), // 1 INR = 0.012 USD approx.
            "EUR", new BigDecimal("1.1")   // 1 EUR = 1.1 USD approx.
    );

    public SalaryQueryService(SalaryRecordRepository repository) {
        this.repository = repository;
    }

    public List<SalaryDTO> getSalariesByYear(int year) {
        List<SalaryRecord> records = repository.findByYear(year);

        // Group by salaryType for aggregation if needed per salary component
        return records.stream()
                .map(this::convertToBaseCurrency)
                .collect(Collectors.toList());
    }

    private SalaryDTO convertToBaseCurrency(SalaryRecord record) {
        BigDecimal rate = conversionRates.getOrDefault(record.getCurrency(), BigDecimal.ONE);
        BigDecimal amountInUSD = record.getAmount().multiply(rate);

        return new SalaryDTO(
                record.getSalaryComponent(),
                record.getSalaryType(),
                record.getCurrency(),
                record.getAmount(),
                amountInUSD
        );
    }
}
