package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.service.SalaryUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/salaries")
public class SalaryUpdateController {

    private final SalaryUpdateService updateService;

    public SalaryUpdateController(SalaryUpdateService updateService) {
        this.updateService = updateService;
    }

    @PutMapping("/{year}/hike")
    public ResponseEntity<String> applyHike(@PathVariable int year, @RequestParam BigDecimal percent) {
        int updatedCount = updateService.updateSalaryHike(year, percent);
        if (updatedCount == 0) {
            return ResponseEntity.badRequest().body("No records found for year " + year);
        }
        return ResponseEntity.ok("Updated " + updatedCount + " salary records with " + percent + "% hike");
    }
}
