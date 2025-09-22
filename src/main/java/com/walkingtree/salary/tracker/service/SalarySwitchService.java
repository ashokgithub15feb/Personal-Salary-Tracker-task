package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.SalarySwitchDTO;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class SalarySwitchService {

    private final SalaryRecordRepository repository;

    public SalarySwitchService(SalaryRecordRepository repository) {
        this.repository = repository;
    }

    public SalaryRecord addNewSalaryRecord(SalarySwitchDTO dto) {
        SalaryRecord record = new SalaryRecord();
        record.setCompany(dto.getCompany());
        record.setCurrency(dto.getCurrency());
        record.setYear(dto.getYear());
        record.setSalaryComponent(dto.getSalaryComponent());
        record.setSalaryType(dto.getSalaryType());
        record.setAmount(dto.getAmount());

        return repository.save(record);
    }
}
