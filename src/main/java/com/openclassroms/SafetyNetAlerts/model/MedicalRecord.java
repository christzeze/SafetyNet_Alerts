package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class MedicalRecord {
    @Id

    private int id;
    private String firstName;
    private String lastName;
    private String birthdate;
    @ElementCollection
    private List<String> medications;
    @ElementCollection
    private List<String> allergies;

    /**
     * Constructeur
     */
    public MedicalRecord(int id,String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    public MedicalRecord() {
    }


}
