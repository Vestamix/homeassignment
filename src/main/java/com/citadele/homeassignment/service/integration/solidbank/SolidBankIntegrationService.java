package com.citadele.homeassignment.service.integration.solidbank;

import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.response.LoanResponseDTO;
import com.citadele.homeassignment.controller.dto.response.SolidBankApplicationResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface SolidBankIntegrationService {
    Optional<ResponseEntity<SolidBankApplicationResponseDTO>> sendApplication(LoanApplicationDTO loanApplicationDTO);

    Optional<ResponseEntity<LoanResponseDTO>> getApplicationInfo(String applicationId);
}
