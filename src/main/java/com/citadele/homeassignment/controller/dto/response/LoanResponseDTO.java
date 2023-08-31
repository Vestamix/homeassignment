package com.citadele.homeassignment.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LoanResponseDTO {
    private final String id;
    private final ApplicationStatus status;
    private final LoanOfferResponeDTO offer;
    @With
    private final String bankName;
}
