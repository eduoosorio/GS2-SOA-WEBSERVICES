package com.futurework.hub.auth.service;

import com.futurework.hub.auth.dto.AuthResponse;
import com.futurework.hub.auth.dto.LoginRequest;
import com.futurework.hub.auth.dto.RegisterRequest;
import com.futurework.hub.auth.dto.UserProfileSummary;
import com.futurework.hub.domain.user.CapabilityProfile;
import com.futurework.hub.domain.user.ContactInfo;
import com.futurework.hub.domain.user.Role;
import com.futurework.hub.domain.user.RoleType;
import com.futurework.hub.domain.user.UserAccount;
import com.futurework.hub.repository.RoleRepository;
import com.futurework.hub.repository.UserAccountRepository;
import com.futurework.hub.shared.exception.BusinessException;
import com.futurework.hub.shared.security.JwtTokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserAccountRepository userAccountRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenService jwtTokenService,
                       AuthenticationManager authenticationManager) {
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userAccountRepository.existsByEmail(request.email())) {
            throw new BusinessException("Já existe um usuário com este e-mail");
        }

        if (userAccountRepository.existsByUsername(request.username())) {
            throw new BusinessException("Já existe um usuário com este username");
        }

        Role defaultRole = roleRepository.findByName(RoleType.TALENT)
                .orElseThrow(() -> new IllegalStateException("Role TALENT não configurada"));

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPreferredContactEmail(Optional.ofNullable(request.preferredContactEmail()).orElse(request.email()));
        contactInfo.setPreferredChannel(Optional.ofNullable(request.preferredChannel()).orElse("email"));
        contactInfo.setLinkedInUrl(request.linkedInUrl());

        UserAccount user = new UserAccount();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setHeadline(request.headline());
        user.setCreatedAt(Instant.now());
        user.setContactInfo(contactInfo);
        HashSet<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        CapabilityProfile profile = new CapabilityProfile();
        profile.setCurrentRole("Talento em transição");
        profile.setFutureGoal("Explorar novos caminhos no futuro do trabalho");
        profile.setPreferredMode(request.preferredWorkMode());
        profile.setAutomationResilienceScore(80);
        profile.setFocusSkills(Optional.ofNullable(request.focusSkills())
                .map(HashSet::new)
                .orElse(new HashSet<>()));
        profile.setUser(user);

        user.setCapabilityProfile(profile);

        UserAccount saved = userAccountRepository.save(user);
        String token = jwtTokenService.generateToken(saved);
        return new AuthResponse(token, toSummary(saved));
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password())
        );

        UserAccount user = (UserAccount) authentication.getPrincipal();

        String token = jwtTokenService.generateToken(user);
        return new AuthResponse(token, toSummary(user));
    }

    private UserProfileSummary toSummary(UserAccount userAccount) {
        return new UserProfileSummary(
                userAccount.getId(),
                userAccount.getFullName(),
                userAccount.getEmail(),
                userAccount.getHeadline(),
                userAccount.getCapabilityProfile() != null ? userAccount.getCapabilityProfile().getPreferredMode() : null,
                userAccount.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet())
        );
    }
}

