package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class Email {
    private String email;
    private String city;

    public Email(String email, String city) {
        this.email = email;
        this.city = city;
    }

    public Email() {
    }
}
