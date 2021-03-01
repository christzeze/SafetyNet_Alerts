package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.List;

@Data
public class FireStationsFlood {
    private String firstName;
    @Id
    private String lastName;
    private String address;
    private long age;
    private String phone;
    private List<String> medications;
    private List<String> allergies;
    private List<String> station;

    public FireStationsFlood() {
    }

    public FireStationsFlood(String firstName, String lastName, String address, long age, String phone, List<String> medications, List<String> allergies, List<String> station) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.age = age;
        this.phone = phone;
        this.medications = medications;
        this.allergies = allergies;
        this.station = station;
    }
}
