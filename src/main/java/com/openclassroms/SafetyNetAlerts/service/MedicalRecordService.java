package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MedicalRecordService {

    public Iterable<MedicalRecord> listAllMedicalRecords();

    public MedicalRecord save(MedicalRecord medicalRecord);

    public Iterable<MedicalRecord> save(List<MedicalRecord> medicalRecords);

    public ResponseEntity deleteMedicalRecord(String firstName,String lastName);

    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecordDetails);

    public MedicalRecord save(String firstName, String lastName, MedicalRecord medicalRecordDetails);
}
