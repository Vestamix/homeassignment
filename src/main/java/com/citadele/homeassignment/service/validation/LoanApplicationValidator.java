package com.citadele.homeassignment.service.validation;

import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.MaritalStatus;
import com.citadele.homeassignment.controller.exception.LoanApplicationValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.regex.Pattern;

@Component
public class LoanApplicationValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "\\+[0-9]{11,15}";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    public void validateDto(LoanApplicationDTO loanApplicationDTO) throws LoanApplicationValidationException {
        var amountValidationMessage = "%s must be equal to or greater than zero";
        var amount = loanApplicationDTO.getAmount();
        if (checkAmountBiggerOrEqualsZero(amount)) {
            throw new LoanApplicationValidationException(amountValidationMessage.formatted("Amount"));
        }
        var monthlyExpenses = loanApplicationDTO.getMonthlyExpenses();
        if (checkAmountBiggerOrEqualsZero(monthlyExpenses)) {
            throw new LoanApplicationValidationException(amountValidationMessage.formatted("Monthly expenses"));
        }
        var monthlyIncome = loanApplicationDTO.getMonthlyIncome();
        if (checkAmountBiggerOrEqualsZero(monthlyIncome)) {
            throw new LoanApplicationValidationException(amountValidationMessage.formatted("Monthly income"));
        }
        var monthlyCreditLiabilities = loanApplicationDTO.getMonthlyCreditLiabilities();
        if (checkAmountBiggerOrEqualsZero(monthlyCreditLiabilities)) {
            throw new LoanApplicationValidationException(amountValidationMessage.formatted("Monthly credit liabilities"));
        }

        var email = loanApplicationDTO.getEmail();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new LoanApplicationValidationException("Please enter a valid email address");
        }
        var phoneNumber = loanApplicationDTO.getPhoneNumber();
        if (!PHONE_PATTERN.matcher(String.valueOf(phoneNumber)).matches()) {
            throw new LoanApplicationValidationException("Please enter a valid phone number");
        }
        var dependents = loanApplicationDTO.getDependents();
        if (dependents < 0) {
            throw new LoanApplicationValidationException(amountValidationMessage.formatted("Dependents"));
        }
        var maritalStatus = loanApplicationDTO.getMaritalStatus();
        if (!EnumSet.allOf(MaritalStatus.class).contains(maritalStatus)) {
            throw new LoanApplicationValidationException("Invalid marital status");
        }
    }

    private boolean checkAmountBiggerOrEqualsZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
}
