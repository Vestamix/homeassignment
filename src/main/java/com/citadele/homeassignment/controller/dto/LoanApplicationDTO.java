package com.citadele.homeassignment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class LoanApplicationDTO {
    private final String phoneNumber;
    private final String email;
    private final BigDecimal monthlyIncome;
    private final BigDecimal monthlyExpenses;
    private final BigDecimal monthlyCreditLiabilities;
    private final Integer dependents;
    private final MaritalStatus maritalStatus;
    private final boolean agreeToBeScored;
    private final boolean agreeToDataSharing;
    private final BigDecimal amount;
}
