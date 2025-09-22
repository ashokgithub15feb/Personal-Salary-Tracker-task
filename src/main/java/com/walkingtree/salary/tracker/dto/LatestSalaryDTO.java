package com.walkingtree.salary.tracker.dto;

import java.math.BigDecimal;

public record LatestSalaryDTO(
        int year,
        String company,
        String currency,
        BigDecimal originalAmount,
        BigDecimal convertedAmount,
        String salaryComponent,
        String salaryType) {

}