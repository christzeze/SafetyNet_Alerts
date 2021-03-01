package com.openclassroms.SafetyNetAlerts.Repository;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroms.SafetyNetAlerts.service.MedicalRecordServiceImpl;
import org.junit.FixMethodOrder;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("com.openclassroms.SafetyNetAlerts")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)


public class MedicalRecordRepositoryTest {

    @Autowired
    private MedicalRecordServiceImpl medicalRecordServiceImpl;


    List<String> medications=new ArrayList<>();
    List<String> allergies=new ArrayList<>();


    @Test
    public void shouldReturnMedicationsSizeWhenSave() {
        //GIVEN
        String firstName="John";
        String lastName="Boyd";
        medications.add("test1");
        medications.add("test2");
        allergies.add("test3");
        MedicalRecord medicalRecordDetails=new MedicalRecord( LocalDate.of(1984, 3, 6),medications, allergies);

        //WHEN
        MedicalRecord medicalRecord=medicalRecordServiceImpl.save(firstName,lastName,medicalRecordDetails);

        //THEN
        assertThat(medicalRecord.getMedications().size()).isEqualTo(2);
        assertThat(medicalRecord.getAllergies().size()).isEqualTo(1);

    }

    @Test
    public void shouldReturnMedicationSizeWhenUpdate() {
        //GIVEN
        String firstName="John";
        String lastName="Boyd";
        medications.add("test1");
        medications.add("test2");
        allergies.add("test3");
        MedicalRecord medicalRecordDetails=new MedicalRecord( LocalDate.of(1994, 3, 6),medications, allergies);

        //WHEN
        MedicalRecord medicalRecord=medicalRecordServiceImpl.updateMedicalRecord(firstName,lastName,medicalRecordDetails);

        //THEN
        assertThat(medicalRecord.getMedications().size()).isEqualTo(2);
        assertThat(medicalRecord.getAllergies().size()).isEqualTo(1);

    }



}
