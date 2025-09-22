package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.dto.CompanySalaryComparison;
import com.walkingtree.salary.tracker.service.SalaryComparisonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/salaries")
public class SalaryComparisonController {

    private final SalaryComparisonService comparisonService;

    public SalaryComparisonController(SalaryComparisonService comparisonService) {
        this.comparisonService = comparisonService;
    }

    @GetMapping("/compare")
    public List<CompanySalaryComparison> compareSalaries(@RequestParam(defaultValue = "USD") String baseCurrency) {
        return comparisonService.compareAverageSalaries(baseCurrency);
    }
}
