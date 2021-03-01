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
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

    private int idCounter;

    private MedicalRecordServiceImpl medicalRecordServiceImpl2;

    public bReadJsonForMedicalRecord(MedicalRecordServiceImpl medicalRecordServiceImpl2) {
        this.medicalRecordServiceImpl2 = medicalRecordServiceImpl2;
    }

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("aReadJsonForPersons");

    @PostConstruct
    public void initDataHandlerJsonFile() throws IOException, ParseException {
        this.loadFile();
        if (object == null) {

        } else {

            shareData(this.object);

            initMedicalRecord();

        }
    }

    /**
     * Chargement des données JSON
     */
    public void loadFile()  {
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
            idCounter=1;
            int idPerson=0;

            Iterator<JSONObject> iterator = medicalRecords.iterator();
            while (iterator.hasNext()) {
                ArrayList<String> listeMedications;
                ArrayList<String> listeAllergies;
                JSONObject medicalRecord = iterator.next();
                String firstName = (String) medicalRecord.get("firstName");
                String lastName = (String) medicalRecord.get("lastName");

                List<Person> persons=personRepository.findPersonByFirstNameAndLastName(firstName,lastName);
                for(Person person:persons) {
                    idPerson=person.getId();
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate birthdate = LocalDate.parse((String) medicalRecord.get("birthdate"), formatter);
                List<String>  medications=(List<String>) medicalRecord.get("medications");
                List<String>  allergies=(List<String>) medicalRecord.get("allergies");
                //Extraction of data of medication and allergies
                listeMedications = medications.stream().collect(Collectors.toCollection(ArrayList::new));
                listeAllergies = allergies.stream().collect(Collectors.toCollection(ArrayList::new));
                this.allMedicalRecords = new ArrayList<>();
                MedicalRecord myMedicalRecord=new MedicalRecord(idCounter,idPerson, birthdate,listeMedications,listeAllergies);
                allMedicalRecords.add(myMedicalRecord);
                medicalRecordServiceImpl2.save(allMedicalRecords);
                idCounter+=1;
            }

            logger.info("Succes of loading Medical Records");
        } else {
            logger.error("Fail of Creation Medical Records");
        }

    }
}
