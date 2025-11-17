package com.futurework.hub.domain.mentorship;

import com.futurework.hub.domain.user.UserAccount;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mentorship_requests")
public class MentorshipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mentee_id")
    private UserAccount mentee;

    private String topic;

    private String desiredOutcome;

    @Enumerated(EnumType.STRING)
    private MentorshipStatus status;

    public MentorshipRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount getMentee() {
        return mentee;
    }

    public void setMentee(UserAccount mentee) {
        this.mentee = mentee;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDesiredOutcome() {
        return desiredOutcome;
    }

    public void setDesiredOutcome(String desiredOutcome) {
        this.desiredOutcome = desiredOutcome;
    }

    public MentorshipStatus getStatus() {
        return status;
    }

    public void setStatus(MentorshipStatus status) {
        this.status = status;
    }
}

