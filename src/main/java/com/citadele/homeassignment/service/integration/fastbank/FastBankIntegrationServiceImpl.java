package com.citadele.homeassignment.service.integration.fastbank;

import com.citadele.homeassignment.controller.dto.FastBankApplicationDTO;
import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.response.FastBankApplicationResponseDTO;
import com.citadele.homeassignment.controller.dto.response.LoanResponseDTO;
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
public class FastBankIntegrationServiceImpl implements FastBankIntegrationService {
    @Value("${integration.api.fastbank.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;

    @Override
    public Optional<ResponseEntity<FastBankApplicationResponseDTO>> sendApplication(LoanApplicationDTO loanApplicationDTO) {
        var fastBankApplicationDTO = new FastBankApplicationDTO(loanApplicationDTO);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var requestEntity = new HttpEntity<>(fastBankApplicationDTO, headers);

        ResponseEntity<FastBankApplicationResponseDTO> applicationResponse;
        try {
            applicationResponse = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, FastBankApplicationResponseDTO.class);
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
            response = restTemplate.exchange(apiUrl + "/" + applicationId, HttpMethod.GET, null, LoanResponseDTO.class);
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
}
