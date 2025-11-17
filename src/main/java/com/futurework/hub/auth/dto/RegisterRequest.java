package com.futurework.hub.auth.dto;

import com.futurework.hub.shared.enums.WorkMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RegisterRequest(
        @NotBlank String fullName,
        @Email String email,
        @NotBlank String username,
        @NotBlank String password,
        String headline,
        @Email String preferredContactEmail,
        String preferredChannel,
        String linkedInUrl,
        @NotNull WorkMode preferredWorkMode,
        Set<String> focusSkills
) {
}

