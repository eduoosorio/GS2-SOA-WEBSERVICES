package com.futurework.hub.domain.user;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;

@Embeddable
public class ContactInfo {

    @Email
    private String preferredContactEmail;

    private String preferredChannel;

    private String phone;

    private String linkedInUrl;

    public ContactInfo() {
    }

    public ContactInfo(String preferredContactEmail, String preferredChannel, String phone, String linkedInUrl) {
        this.preferredContactEmail = preferredContactEmail;
        this.preferredChannel = preferredChannel;
        this.phone = phone;
        this.linkedInUrl = linkedInUrl;
    }

    public String getPreferredContactEmail() {
        return preferredContactEmail;
    }

    public void setPreferredContactEmail(String preferredContactEmail) {
        this.preferredContactEmail = preferredContactEmail;
    }

    public String getPreferredChannel() {
        return preferredChannel;
    }

    public void setPreferredChannel(String preferredChannel) {
        this.preferredChannel = preferredChannel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }
}

