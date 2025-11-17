package com.futurework.hub.profile.service;

import com.futurework.hub.domain.mentorship.MentorshipRequest;
import com.futurework.hub.domain.mentorship.MentorshipStatus;
import com.futurework.hub.domain.user.CapabilityProfile;
import com.futurework.hub.domain.user.ContactInfo;
import com.futurework.hub.domain.user.UserAccount;
import com.futurework.hub.profile.dto.MentorshipRequestPayload;
import com.futurework.hub.profile.dto.ProfileResponse;
import com.futurework.hub.profile.dto.ProfileUpdateRequest;
import com.futurework.hub.repository.MentorshipRequestRepository;
import com.futurework.hub.repository.UserAccountRepository;
import com.futurework.hub.shared.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final UserAccountRepository userAccountRepository;
    private final MentorshipRequestRepository mentorshipRequestRepository;

    public ProfileService(UserAccountRepository userAccountRepository,
                          MentorshipRequestRepository mentorshipRequestRepository) {
        this.userAccountRepository = userAccountRepository;
        this.mentorshipRequestRepository = mentorshipRequestRepository;
    }

    public ProfileResponse me(UserAccount userAccount) {
        UserAccount refreshed = userAccountRepository.findById(userAccount.getId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        return toResponse(refreshed);
    }

    @Transactional
    public ProfileResponse update(UserAccount userAccount, ProfileUpdateRequest request) {
        UserAccount managed = userAccountRepository.findById(userAccount.getId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        managed.setHeadline(request.headline());
        ContactInfo contactInfo = Optional.ofNullable(managed.getContactInfo())
                .orElseGet(() -> {
                    ContactInfo info = new ContactInfo();
                    info.setPreferredContactEmail(managed.getEmail());
                    managed.setContactInfo(info);
                    return info;
                });
        contactInfo.setPreferredChannel(Optional.ofNullable(request.preferredChannel()).orElse("email"));
        contactInfo.setLinkedInUrl(request.linkedInUrl());

        CapabilityProfile profile = Optional.ofNullable(managed.getCapabilityProfile())
                .orElseGet(() -> {
                    CapabilityProfile newProfile = new CapabilityProfile();
                    newProfile.setUser(managed);
                    managed.setCapabilityProfile(newProfile);
                    return newProfile;
                });

        profile.setFutureGoal(request.futureGoal());
        profile.setPreferredMode(request.preferredMode());
        profile.setAutomationResilienceScore(request.automationResilienceScore());
        profile.setFocusSkills(Optional.ofNullable(request.focusSkills())
                .map(HashSet::new)
                .orElse(new HashSet<>()));

        userAccountRepository.save(managed);
        return toResponse(managed);
    }

    public ProfileResponse requestMentorship(UserAccount userAccount, MentorshipRequestPayload payload) {
        UserAccount managed = userAccountRepository.findById(userAccount.getId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        MentorshipRequest request = new MentorshipRequest();
        request.setMentee(managed);
        request.setTopic(payload.topic());
        request.setDesiredOutcome(payload.desiredOutcome());
        request.setStatus(MentorshipStatus.REQUESTED);

        mentorshipRequestRepository.save(request);
        return me(managed);
    }

    private ProfileResponse toResponse(UserAccount userAccount) {
        CapabilityProfile profile = userAccount.getCapabilityProfile();
        Set<ProfileResponse.MentorshipSummary> mentorships = mentorshipRequestRepository.findByMentee(userAccount)
                .stream()
                .map(req -> new ProfileResponse.MentorshipSummary(req.getId(), req.getTopic(), req.getStatus()))
                .collect(Collectors.toSet());

        return new ProfileResponse(
                userAccount.getId(),
                userAccount.getFullName(),
                userAccount.getEmail(),
                userAccount.getHeadline(),
                profile != null ? profile.getFutureGoal() : null,
                profile != null ? profile.getPreferredMode() : null,
                profile != null ? profile.getAutomationResilienceScore() : null,
                profile != null ? profile.getFocusSkills() : Set.of(),
                userAccount.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()),
                mentorships
        );
    }
}

