package com.openclassroms.SafetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "personId", referencedColumnName = "id")
    private Person person;
    @JsonFormat(pattern="MM/dd/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthdate;
    @ElementCollection
    private List<String> medications;
    @ElementCollection
    private List<String> allergies;

    public MedicalRecord(int id, Person abstractPerson, LocalDate birthdate, List<String> medications, List<String> allergies) {
        this.id=id;
        this.person= abstractPerson;
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
