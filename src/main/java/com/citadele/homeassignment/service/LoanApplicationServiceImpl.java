package com.citadele.homeassignment.service;

import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.response.FastBankApplicationResponseDTO;
import com.citadele.homeassignment.controller.dto.response.LoanApplicationResponseDTO;
import com.citadele.homeassignment.controller.dto.response.LoanResponseDTO;
import com.citadele.homeassignment.controller.dto.response.SolidBankApplicationResponseDTO;
import com.citadele.homeassignment.controller.exception.LoanApplicationException;
import com.citadele.homeassignment.controller.exception.LoanApplicationValidationException;
import com.citadele.homeassignment.repository.LoanApplicationRepository;
import com.citadele.homeassignment.repository.entity.LoanApplicationEntity;
import com.citadele.homeassignment.service.integration.fastbank.FastBankIntegrationService;
import com.citadele.homeassignment.service.integration.solidbank.SolidBankIntegrationService;
import com.citadele.homeassignment.service.validation.LoanApplicationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.citadele.homeassignment.controller.dto.response.ApplicationStatus.DRAFT;
import static com.citadele.homeassignment.controller.dto.response.ApplicationStatus.PROCESSED;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {
    private final SolidBankIntegrationService solidBankIntegrationService;
    private final FastBankIntegrationService fastBankIntegrationService;
    private final LoanApplicationRepository applicationRepository;
    @Value("${integration.api.solidbank.name}")
    private String solidBankName;
    @Value("${integration.api.fastbank.name}")
    private String fastBankName;

    @Override
    public Long submitApplication(LoanApplicationDTO loanApplicationDTO) throws LoanApplicationValidationException, LoanApplicationException {
        var validator = new LoanApplicationValidator();
        validator.validateDto(loanApplicationDTO);

        var application = new LoanApplicationEntity();

        var solidBankApplicationOpt = solidBankIntegrationService.sendApplication(loanApplicationDTO)
                .map(HttpEntity::getBody)
                .map(SolidBankApplicationResponseDTO::getId);
        solidBankApplicationOpt.ifPresent(application::setSolidBankApplicationId);


        var fastBankApplicationOpt = fastBankIntegrationService.sendApplication(loanApplicationDTO)
                .map(HttpEntity::getBody)
                .map(FastBankApplicationResponseDTO::getId);
        fastBankApplicationOpt.ifPresent(application::setFastBankApplicationId);

        if (application.getSolidBankApplicationId() == null && application.getFastBankApplicationId() == null) {
            throw new LoanApplicationException();
        }

        application = applicationRepository.save(application);
        return application.getId();
    }

    @Override
    public LoanApplicationResponseDTO trackApplication(Long id) throws LoanApplicationException {
        var applicationOpt = applicationRepository.findById(id);
        var application = applicationOpt.orElseThrow(LoanApplicationException::new);

        var solidBankResponseOpt = solidBankIntegrationService.getApplicationInfo(application.getSolidBankApplicationId());
        var fastBankResponseOpt = fastBankIntegrationService.getApplicationInfo(application.getFastBankApplicationId());

        var solidBankStatus = solidBankResponseOpt
                .map(HttpEntity::getBody)
                .map(LoanResponseDTO::getStatus)
                .orElse(PROCESSED);
        var fastBankStatus = fastBankResponseOpt
                .map(HttpEntity::getBody)
                .map(LoanResponseDTO::getStatus)
                .orElse(PROCESSED);

        var status = solidBankStatus == PROCESSED && fastBankStatus == PROCESSED
                ? PROCESSED
                : DRAFT;

        List<LoanResponseDTO> loanResponses = new ArrayList<>();
        solidBankResponseOpt.map(response -> response.getBody().withBankName(solidBankName))
                .ifPresent(loanResponses::add);
        fastBankResponseOpt.map(response -> response.getBody().withBankName(fastBankName))
                .ifPresent(loanResponses::add);
        return new LoanApplicationResponseDTO(status, loanResponses);
    }
}
