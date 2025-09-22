package com.walkingtree.salary.tracker.repository;

import com.walkingtree.salary.tracker.model.SalaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalaryRecordRepository extends JpaRepository<SalaryRecord, Long> {

    List<SalaryRecord> findByYear(int year);

    // Optional: find the max year to get latest salary
    @Query("select max(s.year) from SalaryRecord s")
    Integer findMaxYear();
}
