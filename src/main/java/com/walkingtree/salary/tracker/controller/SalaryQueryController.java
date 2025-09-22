package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.dto.SalaryDTO;
import com.walkingtree.salary.tracker.service.SalaryQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/salaries")
public class SalaryQueryController {

    private final SalaryQueryService queryService;

    public SalaryQueryController(SalaryQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{year}")
    public List<SalaryDTO> getSalariesByYear(@PathVariable int year) {
        return queryService.getSalariesByYear(year);
    }
}
