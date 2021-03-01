package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroms.SafetyNetAlerts.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class CalculateAgeService {
    private final MedicalRecordRepository medicalRecordRepository;


    public CalculateAgeService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }



    @Autowired
    MedicalRecordServiceImpl medicalRecordServiceImpl2;

    public int calculateAge(LocalDate date) {
        LocalDate now = LocalDate.now();
        return Period.between(date, now).getYears();
    }

    public boolean isChild(int age) {
        return age <= 18;
    }


    public boolean isChild(MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return false;
        }
        int age = calculateAge(medicalRecord.getBirthdate());
        return isChild(age);
    }

    public boolean isAdult(MedicalRecord medicalRecord) {
        return !isChild(medicalRecord);
    }

    public boolean isAdult(int age) {
        return !isChild(age);
    }

}
