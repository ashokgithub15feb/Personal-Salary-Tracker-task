package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SalaryUpdateService {

    private final SalaryRecordRepository repository;

    public SalaryUpdateService(SalaryRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public int updateSalaryHike(int year, BigDecimal hikePercent) {
        List<SalaryRecord> records = repository.findByYear(year);

        BigDecimal multiplier = BigDecimal.ONE.add(hikePercent.divide(BigDecimal.valueOf(100)));

        records.forEach(record -> {
            BigDecimal updatedAmount = record.getAmount().multiply(multiplier);
            record.setAmount(updatedAmount);
            repository.save(record); // can be optimized with batch save in production
        });

        return records.size(); // return count of updated records
    }
}
