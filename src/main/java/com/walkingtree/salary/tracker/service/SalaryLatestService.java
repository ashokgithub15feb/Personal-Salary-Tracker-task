package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.LatestSalaryDTO;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalaryLatestService {

    private final SalaryRecordRepository repository;

    private static final Map<String, BigDecimal> conversionRates = Map.of(
            "USD", BigDecimal.ONE,
            "INR", new BigDecimal("0.012"),
            "AED", new BigDecimal("0.27") // Example conversion rate to AED
    );

    public SalaryLatestService(SalaryRecordRepository repository) {
        this.repository = repository;
    }

    public List<LatestSalaryDTO> getLatestSalary(String baseCurrency) {
        Integer latestYear = repository.findMaxYear();
        if (latestYear == null) {
            return List.of(); // no data
        }

        BigDecimal baseRate = conversionRates.getOrDefault(baseCurrency.toUpperCase(), BigDecimal.ONE);
        List<SalaryRecord> latestRecords = repository.findByYear(latestYear);

        return latestRecords.stream()
                .map(record -> {
                    BigDecimal rate = conversionRates.getOrDefault(record.getCurrency(), BigDecimal.ONE);
                    BigDecimal convertedAmount = record.getAmount().multiply(rate).divide(baseRate, 4, BigDecimal.ROUND_HALF_UP);

                    return new LatestSalaryDTO(
                            record.getYear(),
                            record.getCompany(),
                            record.getCurrency(),
                            record.getAmount(),
                            convertedAmount,
                            record.getSalaryComponent(),
                            record.getSalaryType()
                    );
                })
                .collect(Collectors.toList());
    }
}
