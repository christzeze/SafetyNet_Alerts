package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
@Data
public class PersonInfos {

    private String firstName;
    @Id
    private String lastName;
    private String address;
    private long age;
    private String phone;
    private String email;
    private List<String> medications;
    private List<String> allergies;
    private List<String> station;

    public PersonInfos() {
    }

    public PersonInfos(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
