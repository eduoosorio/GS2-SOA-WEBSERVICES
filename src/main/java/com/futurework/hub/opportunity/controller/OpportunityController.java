package com.futurework.hub.opportunity.controller;

import com.futurework.hub.domain.user.UserAccount;
import com.futurework.hub.opportunity.dto.OpportunityRequest;
import com.futurework.hub.opportunity.dto.OpportunityResponse;
import com.futurework.hub.opportunity.service.OpportunityService;
import com.futurework.hub.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OpportunityResponse>>> listActive() {
        return ResponseEntity.ok(ApiResponse.of("Oportunidades ativas", opportunityService.listActive()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OpportunityResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Oportunidade", opportunityService.findById(id)));
    }

    @GetMapping("/recommended")
    public ResponseEntity<ApiResponse<List<OpportunityResponse>>> recommendations(@AuthenticationPrincipal UserAccount userAccount) {
        return ResponseEntity.ok(ApiResponse.of("Sugestões personalizadas", opportunityService.recommendFor(userAccount)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<OpportunityResponse>> create(@Valid @RequestBody OpportunityRequest request,
                                                                   @AuthenticationPrincipal UserAccount userAccount) {
        OpportunityResponse created = opportunityService.create(request, userAccount);
        return ResponseEntity.ok(ApiResponse.of("Oportunidade criada", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<OpportunityResponse>> update(@PathVariable Long id,
                                                                   @Valid @RequestBody OpportunityRequest request) {
        OpportunityResponse updated = opportunityService.update(id, request);
        return ResponseEntity.ok(ApiResponse.of("Oportunidade atualizada", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        opportunityService.delete(id);
        return ResponseEntity.ok(ApiResponse.of("Oportunidade removida", null));
    }
}

