package com.walkingtree.salary.tracker.dto;

import java.math.BigDecimal;

public class SalaryDTO {

    String salaryComponent;
    String salaryType;
    String currency;
    BigDecimal amount;
    BigDecimal amountInBaseCurrenc;

    public SalaryDTO(String salaryComponent, String salaryType, String currency, BigDecimal amount, BigDecimal amountInBaseCurrenc) {
        this.salaryComponent = salaryComponent;
        this.salaryType = salaryType;
        this.currency = currency;
        this.amount = amount;
        this.amountInBaseCurrenc = amountInBaseCurrenc;
    }

    public String getSalaryComponent() {
        return salaryComponent;
    }

    public void setSalaryComponent(String salaryComponent) {
        this.salaryComponent = salaryComponent;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountInBaseCurrenc() {
        return amountInBaseCurrenc;
    }

    public void setAmountInBaseCurrenc(BigDecimal amountInBaseCurrenc) {
        this.amountInBaseCurrenc = amountInBaseCurrenc;
    }
}
