package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity


public class PersonInfosFull {
    private String firstName;
    @Id
    private String lastName;
    private String address;
    private long age;
    private String phone;
    private String email;
    @ElementCollection(targetClass=String.class)
    private List<String> medications;
    @ElementCollection(targetClass=String.class)
    private List<String> allergies;
    @ElementCollection(targetClass=String.class)
    private List<String> station;

    public PersonInfosFull() {
    }

    public PersonInfosFull(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersonInfosFull(String firstName, String lastName, long age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public PersonInfosFull(String firstName, String lastName, long age, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }
}
