package com.futurework.hub.config;

import com.futurework.hub.domain.user.CapabilityProfile;
import com.futurework.hub.domain.user.ContactInfo;
import com.futurework.hub.domain.user.Role;
import com.futurework.hub.domain.user.RoleType;
import com.futurework.hub.domain.user.UserAccount;
import com.futurework.hub.repository.RoleRepository;
import com.futurework.hub.repository.UserAccountRepository;
import com.futurework.hub.shared.enums.WorkMode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(value = "app.seed-data.enabled", havingValue = "true", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserAccountRepository userAccountRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Stream.of(RoleType.values())
                .forEach(roleType -> roleRepository.findByName(roleType)
                        .orElseGet(() -> roleRepository.save(new Role(roleType))));

        userAccountRepository.findByEmail("admin@futurework.com")
                .orElseGet(this::createDefaultAdmin);
    }

    private UserAccount createDefaultAdmin() {
        HashSet<Role> adminRoles = new HashSet<>();
        adminRoles.add(roleRepository.findByName(RoleType.ADMIN).orElseThrow());
        adminRoles.add(roleRepository.findByName(RoleType.MANAGER).orElseThrow());

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPreferredContactEmail("admin@futurework.com");
        contactInfo.setPreferredChannel("email");
        contactInfo.setLinkedInUrl("https://www.linkedin.com/company/futureworkhub");

        UserAccount admin = new UserAccount();
        admin.setFullName("Admin Futuro do Trabalho");
        admin.setEmail("admin@futurework.com");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setHeadline("Administrador da plataforma Future Work Hub");
        admin.setCreatedAt(Instant.now());
        admin.setContactInfo(contactInfo);
        admin.setRoles(adminRoles);

        CapabilityProfile profile = new CapabilityProfile();
        profile.setCurrentRole("Innovation Strategist");
        profile.setFutureGoal("Criar experiências de trabalho resilientes a automação");
        profile.setPreferredMode(WorkMode.FLEX);
        profile.setAutomationResilienceScore(95);
        profile.setUser(admin);

        admin.setCapabilityProfile(profile);
        return userAccountRepository.save(admin);
    }
}

