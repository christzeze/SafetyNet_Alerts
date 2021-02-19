package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroms.SafetyNetAlerts.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private MedicalRecordRepository medicalRecordRepository;

    /**
     * Constructeur
     */
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository ) {
        this.medicalRecordRepository=medicalRecordRepository;
    }


    /**
     * Liste des informations médicales dans la base
     */

    public Iterable<MedicalRecord> listAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    /**
     * Sauvegarde d'une information médicale dans la base
     */
    public MedicalRecord save(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    /**
     * Sauvegarde de toutes les informations médicales dans la base
     */
    public Iterable<MedicalRecord> save(List<MedicalRecord> medicalRecords) {
        return medicalRecordRepository.saveAll(medicalRecords);

    }
}
