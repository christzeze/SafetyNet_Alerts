package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroms.SafetyNetAlerts.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class CalculateAgeServiceImpl implements CalculateAgeService {
    private final MedicalRecordRepository medicalRecordRepository;


    public CalculateAgeServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Autowired
    MedicalRecordServiceImpl medicalRecordServiceImpl2;

    @Override
    public int calculateAge(LocalDate date) {
        LocalDate now = LocalDate.now();
        return Period.between(date, now).getYears();
    }

    @Override
    public boolean isChild(int age) {
        return age <= 18;
    }

    @Override
        public boolean isChild(MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return false;
        }
        int age = calculateAge(medicalRecord.getBirthdate());
        return isChild(age);
    }

    @Override
    public boolean isAdult(MedicalRecord medicalRecord) {
        return !isChild(medicalRecord);
    }

    @Override
    public boolean isAdult(int age) {
        return !isChild(age);
    }

}
