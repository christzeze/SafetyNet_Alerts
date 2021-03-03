package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;

import java.time.LocalDate;

public interface CalculateAgeService {
    public int calculateAge(LocalDate date);
    public boolean isChild(int age);
    public boolean isChild(MedicalRecord medicalRecord);
    public boolean isAdult(MedicalRecord medicalRecord);
    public boolean isAdult(int age);

}
