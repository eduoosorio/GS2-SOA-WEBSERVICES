package com.futurework.hub.profile.dto;

import com.futurework.hub.shared.enums.WorkMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ProfileUpdateRequest(
        @NotBlank String headline,
        @NotBlank String futureGoal,
        @NotNull WorkMode preferredMode,
        Integer automationResilienceScore,
        Set<String> focusSkills,
        String preferredChannel,
        String linkedInUrl
) {
}

