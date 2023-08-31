package com.citadele.homeassignment.service;

import com.citadele.homeassignment.controller.dto.LoanApplicationDTO;
import com.citadele.homeassignment.controller.dto.MaritalStatus;
import com.citadele.homeassignment.controller.dto.response.*;
import com.citadele.homeassignment.controller.exception.LoanApplicationException;
import com.citadele.homeassignment.repository.LoanApplicationRepository;
import com.citadele.homeassignment.repository.entity.LoanApplicationEntity;
import com.citadele.homeassignment.service.integration.fastbank.FastBankIntegrationService;
import com.citadele.homeassignment.service.integration.solidbank.SolidBankIntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LoanApplicationServiceImplTest {

    @Mock
    private SolidBankIntegrationService solidBankIntegrationService;

    @Mock
    private FastBankIntegrationService fastBankIntegrationService;

    @Mock
    private LoanApplicationRepository applicationRepository;

    @InjectMocks
    private LoanApplicationServiceImpl loanApplicationService;

    private LoanApplicationDTO validDTO;

    @BeforeEach
    public void setup() {
        validDTO = createValidLoanApplicationDTO();
    }

    @Test
    public void testSubmitApplicationWithSolidBankIntegrationSuccess() throws Exception {
        when(solidBankIntegrationService.sendApplication(any()))
                .thenReturn(Optional.of(new ResponseEntity<>(new SolidBankApplicationResponseDTO("123", ApplicationStatus.PROCESSED, null), HttpStatus.OK)));

        LoanApplicationEntity applicationEntity = new LoanApplicationEntity();
        applicationEntity.setId(1L);
        when(applicationRepository.save(any())).thenReturn(applicationEntity);

        Long applicationId = loanApplicationService.submitApplication(validDTO);

        assertEquals(1L, applicationId);
    }

    @Test
    public void testSubmitApplicationWithFastBankIntegrationSuccess() throws Exception {
        when(fastBankIntegrationService.sendApplication(any()))
                .thenReturn(Optional.of(new ResponseEntity<>(new FastBankApplicationResponseDTO("456", ApplicationStatus.PROCESSED, null), HttpStatus.OK)));

        LoanApplicationEntity entity = new LoanApplicationEntity();
        entity.setId(2L);
        when(applicationRepository.save(any())).thenReturn(entity);

        Long applicationId = loanApplicationService.submitApplication(validDTO);

        assertEquals(2L, applicationId);
    }

    @Test
    public void testSubmitApplicationWithBothIntegrationFailures() {
        when(solidBankIntegrationService.sendApplication(any()))
                .thenReturn(Optional.empty());

        when(fastBankIntegrationService.sendApplication(any()))
                .thenReturn(Optional.empty());

        assertThrows(LoanApplicationException.class, () -> loanApplicationService.submitApplication(validDTO));

        verify(applicationRepository, never()).save(any());
    }

    @Test
    public void testSubmitApplicationWithOneIntegrationFailure() throws Exception {
        when(solidBankIntegrationService.sendApplication(any()))
                .thenReturn(Optional.of(new ResponseEntity<>(new SolidBankApplicationResponseDTO("123", ApplicationStatus.PROCESSED, null), HttpStatus.OK)));

        when(fastBankIntegrationService.sendApplication(any()))
                .thenReturn(Optional.of(new ResponseEntity<>(new FastBankApplicationResponseDTO("456", ApplicationStatus.PROCESSED, null), HttpStatus.NOT_FOUND)));

        LoanApplicationEntity applicationEntity = new LoanApplicationEntity();
        applicationEntity.setId(3L);
        when(applicationRepository.save(any())).thenReturn(applicationEntity);

        Long applicationId = loanApplicationService.submitApplication(validDTO);

        assertEquals(3L, applicationId);
    }

    @Test
    public void testTrackApplicationWhenStillProcessing() throws LoanApplicationException {
        var applicationEntity = new LoanApplicationEntity();
        applicationEntity.setSolidBankApplicationId("123");
        applicationEntity.setFastBankApplicationId("456");

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(applicationEntity));
        var solidBankResponse = new LoanResponseDTO("123", ApplicationStatus.PROCESSED, new LoanOfferResponeDTO(), null);
        when(solidBankIntegrationService.getApplicationInfo(any())).thenReturn(Optional.of(new ResponseEntity<>(solidBankResponse, HttpStatus.OK)));
        var fastBankResponse = new LoanResponseDTO("456", ApplicationStatus.DRAFT, new LoanOfferResponeDTO(), null);
        when(fastBankIntegrationService.getApplicationInfo(any())).thenReturn(Optional.of(new ResponseEntity<>(fastBankResponse, HttpStatus.OK)));

        LoanApplicationResponseDTO response = loanApplicationService.trackApplication(1L);

        assertEquals(ApplicationStatus.DRAFT, response.getStatus());
        assertEquals(2, response.getLoanResponses().size());
    }

    @Test
    public void testTrackApplicationWhenOneFailed() throws LoanApplicationException {
        var applicationEntity = new LoanApplicationEntity();
        applicationEntity.setSolidBankApplicationId("123");
        applicationEntity.setFastBankApplicationId("456");

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(applicationEntity));
        var solidBankResponse = new LoanResponseDTO("123", ApplicationStatus.PROCESSED, new LoanOfferResponeDTO(), null);
        when(solidBankIntegrationService.getApplicationInfo(any())).thenReturn(Optional.of(new ResponseEntity<>(solidBankResponse, HttpStatus.OK)));
        when(fastBankIntegrationService.getApplicationInfo(any())).thenReturn(Optional.empty());

        LoanApplicationResponseDTO response = loanApplicationService.trackApplication(1L);

        assertEquals(ApplicationStatus.PROCESSED, response.getStatus());
        assertEquals(1, response.getLoanResponses().size());
    }

    @Test
    public void testTrackApplicationWhenBothFailed() throws LoanApplicationException {
        var applicationEntity = new LoanApplicationEntity();
        applicationEntity.setSolidBankApplicationId("123");
        applicationEntity.setFastBankApplicationId("456");

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(applicationEntity));
        when(solidBankIntegrationService.getApplicationInfo(any())).thenReturn(Optional.empty());
        when(fastBankIntegrationService.getApplicationInfo(any())).thenReturn(Optional.empty());

        LoanApplicationResponseDTO response = loanApplicationService.trackApplication(1L);

        assertEquals(ApplicationStatus.PROCESSED, response.getStatus());
        assertTrue(response.getLoanResponses().isEmpty());
    }

    @Test
    public void testTrackApplicationWhenProcessed() throws LoanApplicationException {
        var applicationEntity = new LoanApplicationEntity();
        applicationEntity.setSolidBankApplicationId("123");
        applicationEntity.setFastBankApplicationId("456");

        when(applicationRepository.findById(2L)).thenReturn(Optional.of(applicationEntity));
        var solidBankResponse = new LoanResponseDTO("123", ApplicationStatus.PROCESSED, new LoanOfferResponeDTO(), null);
        when(solidBankIntegrationService.getApplicationInfo(any())).thenReturn(Optional.of(new ResponseEntity<>(solidBankResponse, HttpStatus.OK)));
        var fastBankResponse = new LoanResponseDTO("456", ApplicationStatus.PROCESSED, new LoanOfferResponeDTO(), null);
        when(fastBankIntegrationService.getApplicationInfo(any())).thenReturn(Optional.of(new ResponseEntity<>(fastBankResponse, HttpStatus.OK)));

        LoanApplicationResponseDTO response = loanApplicationService.trackApplication(2L);

        assertEquals(ApplicationStatus.PROCESSED, response.getStatus());
        assertEquals(2, response.getLoanResponses().size());
    }

    @Test
    public void testThrowsWhenApplicationNotFound() {
        assertThrows(LoanApplicationException.class, () -> loanApplicationService.trackApplication(1L));
    }

    private LoanApplicationDTO createValidLoanApplicationDTO() {
        return new LoanApplicationDTO(
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
    }
}