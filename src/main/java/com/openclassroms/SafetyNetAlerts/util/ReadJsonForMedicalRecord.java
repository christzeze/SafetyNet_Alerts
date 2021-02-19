package com.openclassroms.SafetyNetAlerts.util;

import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroms.SafetyNetAlerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ReadJsonForMedicalRecord {
    private JSONObject object;
    private JSONArray medicalRecords;

    /**
     * Liste des informations médicales de la base de données
     */
    private List<MedicalRecord> allMedicalRecords;

    private int idCounter;

    private MedicalRecordService medicalRecordService;

    public ReadJsonForMedicalRecord(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("ReadJsonForPersons");

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
            Iterator<JSONObject> iterator = medicalRecords.iterator();
            while (iterator.hasNext()) {
                ArrayList<String> listeMedications = new ArrayList<String>();
                ArrayList<String> listeAllergies = new ArrayList<String>();
                JSONObject medicalRecord = iterator.next();
                String firstName = (String) medicalRecord.get("firstName");
                String lastName = (String) medicalRecord.get("lastName");
                String birthdate = (String) medicalRecord.get("birthdate");
                List<String>  medications=(List<String>) medicalRecord.get("medications");
                List<String>  allergies=(List<String>) medicalRecord.get("allergies");
                //Extraction of data of medication and allergies
                for (int i = 0; i < medications.size(); i++) {
                    listeMedications.add((String) medications.get(i));
                }
                for (int i = 0; i < allergies.size(); i++) {
                    listeAllergies.add((String) allergies.get(i));
                }
                this.allMedicalRecords = new ArrayList<>();
                MedicalRecord myMedicalRecord=new MedicalRecord(idCounter,firstName, lastName, birthdate,listeMedications,listeAllergies);
                allMedicalRecords.add(myMedicalRecord);
                medicalRecordService.save(allMedicalRecords);
                idCounter+=1;
            }

            logger.info("Succes of loading Medical Records");
        } else {
            logger.error("Fail of Creation Medical Records");
        }

    }
}
