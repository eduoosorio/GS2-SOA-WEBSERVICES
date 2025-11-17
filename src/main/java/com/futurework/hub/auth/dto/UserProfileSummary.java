package com.futurework.hub.auth.dto;

import com.futurework.hub.shared.enums.WorkMode;

import java.util.Set;

public record UserProfileSummary(
        Long id,
        String fullName,
        String email,
        String headline,
        WorkMode preferredMode,
        Set<String> roles
) {
}

