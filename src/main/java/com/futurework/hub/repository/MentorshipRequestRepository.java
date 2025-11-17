package com.futurework.hub.repository;

import com.futurework.hub.domain.mentorship.MentorshipRequest;
import com.futurework.hub.domain.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorshipRequestRepository extends JpaRepository<MentorshipRequest, Long> {
    List<MentorshipRequest> findByMentee(UserAccount mentee);
}

