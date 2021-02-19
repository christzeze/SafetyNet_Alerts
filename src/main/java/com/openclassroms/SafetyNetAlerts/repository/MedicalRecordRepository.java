package com.openclassroms.SafetyNetAlerts.repository;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;

public interface MedicalRecordRepository extends CrudRepository<MedicalRecord,String> {
}
