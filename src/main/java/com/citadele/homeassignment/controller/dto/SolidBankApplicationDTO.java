package com.citadele.homeassignment.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SolidBankApplicationDTO {
    private final String phone;
    private final String email;
    private final BigDecimal monthlyIncome;
    private final BigDecimal monthlyExpenses;
    private final MaritalStatus maritalStatus;
    private final boolean agreeToBeScored;
    private final BigDecimal amount;

    public SolidBankApplicationDTO(LoanApplicationDTO loanApplicationDTO) {
        this.phone = loanApplicationDTO.getPhoneNumber();
        this.email = loanApplicationDTO.getEmail();
        this.monthlyIncome = loanApplicationDTO.getMonthlyIncome();
        this.monthlyExpenses = loanApplicationDTO.getMonthlyExpenses();
        this.maritalStatus = loanApplicationDTO.getMaritalStatus();
        this.agreeToBeScored = loanApplicationDTO.isAgreeToBeScored();
        this.amount = loanApplicationDTO.getAmount();
    }
}
