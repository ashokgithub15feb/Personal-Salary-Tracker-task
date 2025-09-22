package com.walkingtree.salary.tracker.dto;

import java.math.BigDecimal;

public record YearlySalaryTrend(int year,
                                BigDecimal totalAmountInBaseCurrency) {
}
