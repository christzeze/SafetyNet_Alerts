package com.openclassroms.SafetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int idPerson;
    //private String firstName;
    //private String lastName;
    @JsonFormat(pattern="MM/dd/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthdate;
    @ElementCollection
    private List<String> medications;
    @ElementCollection
    private List<String> allergies;

    /**
     * Constructeur
     */
    public MedicalRecord(int id, int idPerson, LocalDate birthdate, List<String> medications, List<String> allergies) {
        this.id=id;
        //this.firstName = firstName;
        //this.lastName = lastName;
        this.idPerson=idPerson;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    public MedicalRecord(List<String> medications, List<String> allergies) {
        this.medications = medications;
        this.allergies = allergies;
    }

    public MedicalRecord(LocalDate birthdate, List<String> medications, List<String> allergies) {
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    public MedicalRecord(LocalDate birthdate) {
        this.birthdate = birthdate;
    }


    public MedicalRecord() {
    }


}
