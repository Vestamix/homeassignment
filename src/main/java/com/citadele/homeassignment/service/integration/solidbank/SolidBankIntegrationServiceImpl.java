package com.citadele.homeassignment.service.integration.solidbank;

import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.SolidBankApplicationDTO;
import com.citadele.homeassignment.controller.dto.response.LoanResponseDTO;
import com.citadele.homeassignment.controller.dto.response.SolidBankApplicationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolidBankIntegrationServiceImpl implements SolidBankIntegrationService {
    @Value("${integration.api.solidbank.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;

    @Override
    public Optional<ResponseEntity<SolidBankApplicationResponseDTO>> sendApplication(LoanApplicationDTO loanApplicationDTO) {
        var solidBankApplication = new SolidBankApplicationDTO(loanApplicationDTO);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var requestEntity = new HttpEntity<>(solidBankApplication, headers);

        ResponseEntity<SolidBankApplicationResponseDTO> applicationResponse;
        try {
            applicationResponse = restTemplate.exchange(getApiUrl(), HttpMethod.POST, requestEntity, SolidBankApplicationResponseDTO.class);
        } catch (HttpClientErrorException.BadRequest | HttpClientErrorException.NotFound ex) {
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.error(ex.getMessage(), ex);
            } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error(ex.getMessage(), ex);
            }
            applicationResponse = null;
        }
        return Optional.ofNullable(applicationResponse);
    }

    @Override
    public Optional<ResponseEntity<LoanResponseDTO>> getApplicationInfo(String applicationId) {
        ResponseEntity<LoanResponseDTO> response;
        try {
            response = restTemplate.exchange(getApiUrl() + "/" + applicationId, HttpMethod.GET, null, LoanResponseDTO.class);
        } catch (HttpClientErrorException.BadRequest | HttpClientErrorException.NotFound ex) {
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.error(ex.getMessage(), ex);
            } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error(ex.getMessage(), ex);
            }
            response = null;
        }
        return Optional.ofNullable(response);
    }

    String getApiUrl() {
        return apiUrl;
    }
}
