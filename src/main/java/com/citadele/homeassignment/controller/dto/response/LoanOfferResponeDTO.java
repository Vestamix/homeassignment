package com.citadele.homeassignment.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LoanOfferResponeDTO {
    private final BigDecimal monthlyPaymentAmount;
    private final BigDecimal totalRepaymentAmount;
    private final Integer numberOfPayments;
    private final BigDecimal annualPercentageRate;
    private final String firstRepaymentDate;
}
