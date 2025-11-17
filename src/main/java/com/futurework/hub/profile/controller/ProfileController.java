package com.futurework.hub.profile.controller;

import com.futurework.hub.domain.user.UserAccount;
import com.futurework.hub.profile.dto.MentorshipRequestPayload;
import com.futurework.hub.profile.dto.ProfileResponse;
import com.futurework.hub.profile.dto.ProfileUpdateRequest;
import com.futurework.hub.profile.service.ProfileService;
import com.futurework.hub.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> me(@AuthenticationPrincipal UserAccount userAccount) {
        return ResponseEntity.ok(ApiResponse.of("Perfil recuperado", profileService.me(userAccount)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> update(@AuthenticationPrincipal UserAccount userAccount,
                                                               @Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.of("Perfil atualizado", profileService.update(userAccount, request)));
    }

    @PostMapping("/mentorships")
    public ResponseEntity<ApiResponse<ProfileResponse>> mentorship(@AuthenticationPrincipal UserAccount userAccount,
                                                                   @Valid @RequestBody MentorshipRequestPayload payload) {
        return ResponseEntity.ok(ApiResponse.of("Mentoria solicitada", profileService.requestMentorship(userAccount, payload)));
    }
}

