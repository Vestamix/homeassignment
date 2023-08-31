package com.citadele.homeassignment.service.validation;

import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.MaritalStatus;
import com.citadele.homeassignment.controller.exception.LoanApplicationValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class LoanApplicationValidatorTest {
    private final LoanApplicationValidator validator = new LoanApplicationValidator();

    @Test
    public void testValidLoanApplicationDTO() {
        LoanApplicationDTO dto = new LoanApplicationDTO(
                "+37121234567",
                "john@doe.com",
                BigDecimal.TEN,
                BigDecimal.ONE,
                BigDecimal.ONE,
                1,
                MaritalStatus.DIVORCED,
                true,
                false,
                BigDecimal.TEN
        );
        assertDoesNotThrow(() -> validator.validateDto(dto));
    }

    @Test
    public void testInvalidEmail() {
        LoanApplicationDTO dto = LoanApplicationDTO.builder()
                .monthlyIncome(BigDecimal.TEN)
                .amount(BigDecimal.TEN)
                .monthlyExpenses(BigDecimal.TEN)
                .monthlyCreditLiabilities(BigDecimal.TEN)
                .email("InvalidEmail")
                .build();
        assertThrows(LoanApplicationValidationException.class, () -> validator.validateDto(dto));
    }

    @Test
    public void testInvalidPhone() {
        LoanApplicationDTO dto = LoanApplicationDTO.builder()
                .monthlyIncome(BigDecimal.TEN)
                .amount(BigDecimal.TEN)
                .monthlyExpenses(BigDecimal.TEN)
                .monthlyCreditLiabilities(BigDecimal.TEN)
                .email("john@doe.com")
                .phoneNumber("1234")
                .build();
        assertThrows(LoanApplicationValidationException.class, () -> validator.validateDto(dto));
    }

    @Test
    public void testNegativeNumber() {
        LoanApplicationDTO dto = LoanApplicationDTO.builder()
                .amount(BigDecimal.valueOf(-10))
                .build();
        assertThrows(LoanApplicationValidationException.class, () -> validator.validateDto(dto));
    }
}