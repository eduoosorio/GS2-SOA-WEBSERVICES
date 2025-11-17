package com.futurework.hub.auth.dto;

public record AuthResponse(
        String token,
        UserProfileSummary user
) {
}

