package com.openclassroms.SafetyNetAlerts.util;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroms.SafetyNetAlerts.model.Person;
import com.openclassroms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassroms.SafetyNetAlerts.service.MedicalRecordServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class bReadJsonForMedicalRecord {
    private JSONObject object;
    private JSONArray medicalRecords;

    @Autowired
    private PersonRepository personRepository;

    /**
     * Liste des informations médicales de la base de données
     */
    private List<MedicalRecord> allMedicalRecords;

    private static final Logger logger = LogManager.getLogger("aReadJsonForPersons");
    private int idCounter;

    private MedicalRecordServiceImpl medicalRecordServiceImpl2;


    public bReadJsonForMedicalRecord(MedicalRecordServiceImpl medicalRecordServiceImpl2) {
        this.medicalRecordServiceImpl2 = medicalRecordServiceImpl2;
    }

    @PostConstruct
    public void initDataHandlerJsonFile() {
        this.loadFile();
        if (object != null) {
            shareData(this.object);
            initMedicalRecord();
        }
    }

    /**
     * Chargement des données JSON
     */
    public void loadFile() {
        try {
            JSONParser jsonParser = new JSONParser();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("./data.json");
            this.object = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream));
            logger.info("Data loaded");
        } catch (Exception e) {
            logger.error(new StringBuffer("Data can't be loaded in Db : ").append(e));
        }

    }

    public void shareData(JSONObject obj) {

        medicalRecords = (JSONArray) obj.get("medicalrecords");


    }

    /**
     * Sauvegarde des données dans la base
     */
    public void initMedicalRecord() {
        if (medicalRecords != null) {
            idCounter = 1;
            int idPerson = 0;

            Iterator<JSONObject> iterator = medicalRecords.iterator();
            while (iterator.hasNext()) {
                ArrayList<String> listeMedications;
                ArrayList<String> listeAllergies;
                JSONObject medicalRecord = iterator.next();
                String firstName = (String) medicalRecord.get("firstName");
                String lastName = (String) medicalRecord.get("lastName");

                List<Person> abstractPeople = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
                if (CollectionUtils.isEmpty(abstractPeople)) {
                    logger.warn("Aucune personne, je saute cette étape {}", medicalRecord);
                    continue;
                }
                // FIXME: on prend la dernière personne à cause des homonymes mais ce n'est pas bien
                Person Person = abstractPeople.get(abstractPeople.size() - 1);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate birthdate = LocalDate.parse((String) medicalRecord.get("birthdate"), formatter);
                List<String> medications = (List<String>) medicalRecord.get("medications");
                List<String> allergies = (List<String>) medicalRecord.get("allergies");
                //Extraction of data of medication and allergies
                listeMedications = new ArrayList<>(medications);
                listeAllergies = new ArrayList<>(allergies);
                this.allMedicalRecords = new ArrayList<>();
                MedicalRecord myMedicalRecord = new MedicalRecord(idCounter, Person, birthdate, listeMedications, listeAllergies);
                allMedicalRecords.add(myMedicalRecord);
                medicalRecordServiceImpl2.save(allMedicalRecords);
                idCounter += 1;
            }

            logger.info("Succes of loading Medical Records");
        } else {
            logger.error("Fail of Creation Medical Records");
        }

    }
}
