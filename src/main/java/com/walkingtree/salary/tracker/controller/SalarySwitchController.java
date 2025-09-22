package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.dto.SalarySwitchDTO;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.service.SalarySwitchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salaries")
public class SalarySwitchController {

    private final SalarySwitchService switchService;

    public SalarySwitchController(SalarySwitchService switchService) {
        this.switchService = switchService;
    }

    @PostMapping("/switch")
    public ResponseEntity<String> switchCompany(@RequestBody SalarySwitchDTO salarySwitchDTO) {
        SalaryRecord savedRecord = switchService.addNewSalaryRecord(salarySwitchDTO);
        return ResponseEntity.ok("New salary record added for company: " + savedRecord.getCompany());
    }
}
