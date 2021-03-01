package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroms.SafetyNetAlerts.model.Person;
import com.openclassroms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassroms.SafetyNetAlerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
//@Configuration
//@ComponentScan("com.openclassroms.SafetyNetAlerts")

public class MedicalRecordServiceImpl implements MedicalRecordService {


    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PersonRepository personRepository;


    /**
     * Constructeur
     */

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("PersonServiceImpl");

    /**
     * Liste des informations médicales dans la base
     */
    @Override
    public Iterable<MedicalRecord> listAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    /**
     * Sauvegarde d'une information médicale dans la base
     */
    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {

        return medicalRecordRepository.save(medicalRecord);
    }

    /**
     * Sauvegarde de toutes les informations médicales dans la base
     */
    @Override
    public Iterable<MedicalRecord> save(List<MedicalRecord> medicalRecords) {
        return medicalRecordRepository.saveAll(medicalRecords);
    }

    /**
     * Suppression d'un médical record et de la personne associée
     */
    @Override
    public ResponseEntity deleteMedicalRecord(String firstName, String lastName) {
        List<Person> abstractPeople = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
        for (Person Person : abstractPeople) {
            MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByPersonId(Person.getId());
            medicalRecordRepository.delete(medicalRecord);
        }
        for (Person Person : abstractPeople) {
            personRepository.delete(Person);
        }
        if (abstractPeople != null) {
            logger.info("delete  medical record succeeded");
            return ResponseEntity.ok(personRepository);
        } else {
            logger.info("medical record not delete");
            return ResponseEntity.badRequest()
                    .body(personRepository);
        }
    }

    /**
     * mise à jour d'un médical record
     */
    @Override
    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecordDetails) {
        int personId = 0;

        List<Person> abstractPeople = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
        for (Person Person : abstractPeople) {
            personId = Person.getId();
        }
        MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByPersonId(personId);
        Person Person = new Person();
        Person.setId(personId);
        medicalRecord.setPerson(Person);
        medicalRecord.setBirthdate(medicalRecordDetails.getBirthdate());
        medicalRecord.setAllergies(medicalRecordDetails.getAllergies());
        medicalRecord.setMedications(medicalRecordDetails.getMedications());
        final MedicalRecord updateMedicalRecord = medicalRecordRepository.save(medicalRecord);
        if (updateMedicalRecord != null) {
            logger.info("Update  medical record succeeded");
            return updateMedicalRecord;
        } else {
            return null;
        }


    }

    /**
     * ajout d'un médical record
     */
    @Override
    public MedicalRecord save(String firstName, String lastName, MedicalRecord medicalRecordDetails) {
        int id = 0;
        List<Person> abstractPeople = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
        for (Person Person : abstractPeople) {
            id = Person.getId();
        }
        MedicalRecord medicalRecord = new MedicalRecord();
        Person Person = new Person();
        Person.setId(id);
        medicalRecord.setPerson(Person);
        medicalRecord.setBirthdate(medicalRecordDetails.getBirthdate());
        medicalRecord.setMedications(medicalRecordDetails.getMedications());
        medicalRecord.setAllergies(medicalRecordDetails.getAllergies());
        final MedicalRecord saveMedicalRecord = medicalRecordRepository.save(medicalRecord);
        if (saveMedicalRecord == null) {
            logger.info("person not create");
        }
        logger.info("create  medical record succeeded");
        return saveMedicalRecord;
    }


}
