package com.futurework.hub.opportunity.dto;

import com.futurework.hub.shared.enums.WorkMode;

import java.time.LocalDate;
import java.util.Set;

public record OpportunityResponse(
        Long id,
        String title,
        String description,
        String organization,
        WorkMode workMode,
        Integer automationResilienceScore,
        LocalDate closingDate,
        Boolean active,
        Set<String> requiredSkills,
        String createdBy
) {
}

