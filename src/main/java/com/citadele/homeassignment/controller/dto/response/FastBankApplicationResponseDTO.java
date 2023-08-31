package com.citadele.homeassignment.controller.dto.response;

import com.citadele.homeassignment.controller.dto.SolidBankApplicationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class FastBankApplicationResponseDTO {
    private final String id;
    private final ApplicationStatus status;
    private final SolidBankApplicationDTO request;
}
