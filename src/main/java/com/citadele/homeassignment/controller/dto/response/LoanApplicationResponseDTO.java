package com.citadele.homeassignment.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class LoanApplicationResponseDTO {
    private final ApplicationStatus status;
    private final List<LoanResponseDTO> loanResponses;
}
