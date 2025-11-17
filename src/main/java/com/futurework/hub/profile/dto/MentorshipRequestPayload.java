package com.futurework.hub.profile.dto;

import jakarta.validation.constraints.NotBlank;

public record MentorshipRequestPayload(
        @NotBlank String topic,
        String desiredOutcome
) {
}

