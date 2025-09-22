package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.dto.LatestSalaryDTO;
import com.walkingtree.salary.tracker.service.SalaryLatestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SalaryLatestController {

    private final SalaryLatestService latestService;

    public SalaryLatestController(SalaryLatestService latestService) {
        this.latestService = latestService;
    }

    @GetMapping("/latest")
    public List<LatestSalaryDTO> getLatestSalary(@RequestParam(defaultValue = "AED") String baseCurrency) {
        return latestService.getLatestSalary(baseCurrency);
    }
}
