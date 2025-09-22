package com.walkingtree.salary.tracker.dto;

import java.math.BigDecimal;

public record CompanySalaryComparison(
        String company,
        BigDecimal avgSalaryOriginal,
        String originalCurrency,
        BigDecimal avgSalaryInBaseCurrency) {

}