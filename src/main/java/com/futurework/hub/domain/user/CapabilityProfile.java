package com.futurework.hub.domain.user;

import com.futurework.hub.shared.enums.WorkMode;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "capability_profiles")
public class CapabilityProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_role_name")
    private String currentRole;

    private String futureGoal;

    @Enumerated(EnumType.STRING)
    private WorkMode preferredMode;

    private Integer automationResilienceScore;

    @ElementCollection
    @CollectionTable(name = "profile_focus_skills", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skill")
    private Set<String> focusSkills = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount user;

    public CapabilityProfile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public String getFutureGoal() {
        return futureGoal;
    }

    public void setFutureGoal(String futureGoal) {
        this.futureGoal = futureGoal;
    }

    public WorkMode getPreferredMode() {
        return preferredMode;
    }

    public void setPreferredMode(WorkMode preferredMode) {
        this.preferredMode = preferredMode;
    }

    public Integer getAutomationResilienceScore() {
        return automationResilienceScore;
    }

    public void setAutomationResilienceScore(Integer automationResilienceScore) {
        this.automationResilienceScore = automationResilienceScore;
    }

    public Set<String> getFocusSkills() {
        return focusSkills;
    }

    public void setFocusSkills(Set<String> focusSkills) {
        this.focusSkills = focusSkills != null ? focusSkills : new HashSet<>();
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }
}
