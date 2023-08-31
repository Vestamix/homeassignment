package com.citadele.homeassignment.controller;

import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.response.LoanApplicationResponseDTO;
import com.citadele.homeassignment.controller.dto.response.LoanResponseDTO;
import com.citadele.homeassignment.controller.dto.SolidBankApplicationDTO;
import com.citadele.homeassignment.controller.dto.response.SolidBankApplicationResponseDTO;
import com.citadele.homeassignment.controller.exception.LoanApplicationException;
import com.citadele.homeassignment.controller.exception.LoanApplicationValidationException;
import com.citadele.homeassignment.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/application")
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    @PostMapping("/submit")
    public Long submitLoanApplication(@RequestBody LoanApplicationDTO loanApplicationDTO) throws LoanApplicationValidationException, LoanApplicationException {
        return loanApplicationService.submitApplication(loanApplicationDTO);
    }

    @GetMapping("/{applicationId}")
    public LoanApplicationResponseDTO trackLoanApplication(@PathVariable Long applicationId) throws LoanApplicationException {
        return loanApplicationService.trackApplication(applicationId);
    }
}
