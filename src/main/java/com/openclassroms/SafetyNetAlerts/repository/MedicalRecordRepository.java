package com.openclassroms.SafetyNetAlerts.repository;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface MedicalRecordRepository extends CrudRepository<MedicalRecord,Integer> {
    //List<MedicalRecord> findMedicalRecordByFirstNameAndLastName(String firstName, String lastName);
    //MedicalRecord findFirstMedicalRecordByFirstNameAndLastName(String firstName, String lastName);
    MedicalRecord findFirstMedicalRecordByPersonId(int personId);
}
