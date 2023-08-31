package com.citadele.homeassignment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class FastBankApplicationDTO {
    private final String phoneNumber;
    private final String email;
    private final BigDecimal monthlyIncomeAmount;
    private final BigDecimal monthlyCreditLiabilities;
    private final Integer dependents;
    private final boolean agreeToDataSharing;
    private final BigDecimal amount;

    public FastBankApplicationDTO(LoanApplicationDTO loanApplicationDTO) {
        this.phoneNumber = loanApplicationDTO.getPhoneNumber();
        this.email = loanApplicationDTO.getEmail();
        this.monthlyIncomeAmount = loanApplicationDTO.getMonthlyIncome();
        this.monthlyCreditLiabilities = loanApplicationDTO.getMonthlyCreditLiabilities();
        this.dependents = loanApplicationDTO.getDependents();
        this.agreeToDataSharing = loanApplicationDTO.isAgreeToDataSharing();
        this.amount = loanApplicationDTO.getAmount();
    }
}
