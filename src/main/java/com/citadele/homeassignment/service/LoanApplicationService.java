package com.citadele.homeassignment.service;

import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.response.LoanApplicationResponseDTO;
import com.citadele.homeassignment.controller.exception.LoanApplicationException;
import com.citadele.homeassignment.controller.exception.LoanApplicationValidationException;

public interface LoanApplicationService {
    Long submitApplication(LoanApplicationDTO loanApplicationDTO) throws LoanApplicationValidationException, LoanApplicationException;

    LoanApplicationResponseDTO trackApplication(Long id) throws LoanApplicationException;
}
