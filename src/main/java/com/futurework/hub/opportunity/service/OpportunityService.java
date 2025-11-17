package com.futurework.hub.opportunity.service;

import com.futurework.hub.domain.opportunity.WorkOpportunity;
import com.futurework.hub.domain.user.UserAccount;
import com.futurework.hub.opportunity.dto.OpportunityRequest;
import com.futurework.hub.opportunity.dto.OpportunityResponse;
import com.futurework.hub.repository.WorkOpportunityRepository;
import com.futurework.hub.shared.enums.WorkMode;
import com.futurework.hub.shared.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OpportunityService {

    private final WorkOpportunityRepository workOpportunityRepository;

    public OpportunityService(WorkOpportunityRepository workOpportunityRepository) {
        this.workOpportunityRepository = workOpportunityRepository;
    }

    public OpportunityResponse create(OpportunityRequest request, UserAccount creator) {
        WorkOpportunity opportunity = new WorkOpportunity();
        mapToEntity(request, opportunity);
        opportunity.setCreatedBy(creator.getFullName());
        WorkOpportunity saved = workOpportunityRepository.save(opportunity);
        return toResponse(saved);
    }

    public OpportunityResponse update(Long id, OpportunityRequest request) {
        WorkOpportunity existing = workOpportunityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Oportunidade não encontrada"));
        mapToEntity(request, existing);
        return toResponse(workOpportunityRepository.save(existing));
    }

    public void delete(Long id) {
        WorkOpportunity existing = workOpportunityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Oportunidade não encontrada"));
        workOpportunityRepository.delete(existing);
    }

    public List<OpportunityResponse> listActive() {
        return workOpportunityRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OpportunityResponse findById(Long id) {
        return workOpportunityRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Oportunidade não encontrada"));
    }

    public List<OpportunityResponse> recommendFor(UserAccount userAccount) {
        Set<String> focusSkills = userAccount.getCapabilityProfile() != null
                ? userAccount.getCapabilityProfile().getFocusSkills()
                : Set.of();
        WorkMode preferredMode = userAccount.getCapabilityProfile() != null
                ? userAccount.getCapabilityProfile().getPreferredMode()
                : null;

        return workOpportunityRepository.findByActiveTrue()
                .stream()
                .sorted(Comparator.comparingInt((WorkOpportunity op) -> scoreForUser(focusSkills, preferredMode, op)).reversed())
                .limit(5)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private int scoreForUser(Set<String> focusSkills, WorkMode preferredMode, WorkOpportunity opportunity) {
        int score = scoreBySkills(focusSkills, opportunity);
        if (preferredMode != null && preferredMode == opportunity.getWorkMode()) {
            score += 2;
        }
        return score;
    }

    private int scoreBySkills(Set<String> focusSkills, WorkOpportunity opportunity) {
        if (focusSkills == null || focusSkills.isEmpty()) {
            return 0;
        }
        long matches = opportunity.getRequiredSkills()
                .stream()
                .filter(focusSkills::contains)
                .count();
        return (int) matches;
    }

    private void mapToEntity(OpportunityRequest request, WorkOpportunity opportunity) {
        opportunity.setTitle(request.title());
        opportunity.setDescription(request.description());
        opportunity.setOrganization(request.organization());
        opportunity.setWorkMode(request.workMode());
        opportunity.setAutomationResilienceScore(request.automationResilienceScore());
        opportunity.setClosingDate(request.closingDate());
        opportunity.setActive(request.active() != null ? request.active() : Boolean.TRUE);
        opportunity.setRequiredSkills(request.requiredSkills() != null ? new HashSet<>(request.requiredSkills()) : new HashSet<>());
    }

    private OpportunityResponse toResponse(WorkOpportunity opportunity) {
        return new OpportunityResponse(
                opportunity.getId(),
                opportunity.getTitle(),
                opportunity.getDescription(),
                opportunity.getOrganization(),
                opportunity.getWorkMode(),
                opportunity.getAutomationResilienceScore(),
                opportunity.getClosingDate(),
                opportunity.getActive(),
                opportunity.getRequiredSkills(),
                opportunity.getCreatedBy()
        );
    }
}

