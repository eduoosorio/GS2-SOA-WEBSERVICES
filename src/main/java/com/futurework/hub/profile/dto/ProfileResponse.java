package com.futurework.hub.profile.dto;

import com.futurework.hub.domain.mentorship.MentorshipStatus;
import com.futurework.hub.shared.enums.WorkMode;

import java.util.Set;

public record ProfileResponse(
        Long id,
        String fullName,
        String email,
        String headline,
        String futureGoal,
        WorkMode preferredMode,
        Integer automationResilienceScore,
        Set<String> focusSkills,
        Set<String> roles,
        Set<MentorshipSummary> mentorshipRequests
) {
    public record MentorshipSummary(Long id, String topic, MentorshipStatus status) {
    }
}

