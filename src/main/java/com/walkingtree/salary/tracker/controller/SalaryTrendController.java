package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.dto.YearlySalaryTrend;
import com.walkingtree.salary.tracker.service.SalaryTrendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/salaries")
public class SalaryTrendController {

    private final SalaryTrendService trendService;

    public SalaryTrendController(SalaryTrendService trendService) {
        this.trendService = trendService;
    }

    @GetMapping("/trend")
    public List<YearlySalaryTrend> getSalaryTrend(@RequestParam(defaultValue = "USD") String baseCurrency) {
        return trendService.getSalaryGrowthTrend(baseCurrency);
    }
}
