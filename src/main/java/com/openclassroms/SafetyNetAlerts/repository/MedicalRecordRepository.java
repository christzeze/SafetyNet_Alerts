package com.openclassroms.SafetyNetAlerts.repository;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroms.SafetyNetAlerts.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface MedicalRecordRepository extends CrudRepository<MedicalRecord,Integer> {
    //List<MedicalRecord> findMedicalRecordByFirstNameAndLastName(String firstName, String lastName);
    //MedicalRecord findFirstMedicalRecordByFirstNameAndLastName(String firstName, String lastName);
    MedicalRecord findFirstMedicalRecordByIdPerson(int personId);


}
