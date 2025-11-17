package com.futurework.hub.opportunity.dto;

import com.futurework.hub.shared.enums.WorkMode;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record OpportunityRequest(
        @NotBlank String title,
        String description,
        String organization,
        @NotNull WorkMode workMode,
        Integer automationResilienceScore,
        @FutureOrPresent LocalDate closingDate,
        Set<String> requiredSkills,
        Boolean active
) {
}

