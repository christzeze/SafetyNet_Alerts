package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.List;

@Data
public class PhoneCoverage {
    private String phone;

    public PhoneCoverage() {
    }

    public PhoneCoverage(String phone) {
        this.phone = phone;
    }


}
