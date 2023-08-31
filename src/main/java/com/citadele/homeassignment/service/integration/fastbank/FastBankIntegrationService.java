package com.citadele.homeassignment.service.integration.fastbank;

import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.response.FastBankApplicationResponseDTO;
import com.citadele.homeassignment.controller.dto.response.LoanResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface FastBankIntegrationService {
    Optional<ResponseEntity<FastBankApplicationResponseDTO>> sendApplication(LoanApplicationDTO loanApplicationDTO);

    Optional<ResponseEntity<LoanResponseDTO>> getApplicationInfo(String applicationId);
}
