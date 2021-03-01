package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    private long age;
    @ElementCollection(targetClass = String.class)
    private List<String> medications;
    @ElementCollection(targetClass = String.class)
    private List<String> allergies;
    @ElementCollection(targetClass = String.class)
    private List<String> station;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String firstName, String lastName, long age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Person(String firstName, String lastName, long age, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }

    public Person(int id, String firstName, String lastName, String address, String city, String zip, String phone, String mail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.email = mail;
        this.zip = zip;
        this.address = address;
        this.phone = phone;
    }
}
