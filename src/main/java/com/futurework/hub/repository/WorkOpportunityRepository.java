package com.futurework.hub.repository;

import com.futurework.hub.domain.opportunity.WorkOpportunity;
import com.futurework.hub.shared.enums.WorkMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkOpportunityRepository extends JpaRepository<WorkOpportunity, Long> {
    List<WorkOpportunity> findByActiveTrue();

    List<WorkOpportunity> findByWorkMode(WorkMode workMode);
}

