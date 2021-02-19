package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.FireStation;
import com.openclassroms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassroms.SafetyNetAlerts.model.Personn;
import com.openclassroms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassroms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassroms.SafetyNetAlerts.repository.PersonnRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class InformationService {

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("InformationService");

    private PersonService personService;
    private FireStationService fireStationService;
    private MedicalRecordService medicalRecordService;



    public InformationService(PersonService personService, FireStationService fireStationService, MedicalRecordService medicalRecordService) {
        this.personService = personService;
        this.fireStationService = fireStationService;
        this.medicalRecordService = medicalRecordService;
    }

    public StringBuilder getListOfChild(String address) throws ParseException {
        StringBuilder data = new StringBuilder();
        data.append("{\"childs\" : [");
        // lit  les informations pour chaque personne
        for(Personn iPerson:personService.listAllPersonns()) {
            // même adresse que la valeur donnée en paramètre
            if(iPerson.getAddress().equals(address)) {
                // lit  les informations pour chaque medical record
                for (MedicalRecord iMedicalRecord : medicalRecordService.listAllMedicalRecords()) {
                    // même nom et même prénom que personne
                    if (iMedicalRecord.getLastName().equals(iPerson.getLastName()) & iMedicalRecord.getFirstName().equals(iPerson.getFirstName())) {
                        // calcul age de la personne
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        String myDate = iMedicalRecord.getBirthdate();
                        LocalDate dateAnniversaire = LocalDate.parse(myDate, dateFormat);
                        String caseStartDate = dateFormat.format(LocalDateTime.now());
                        LocalDate aujourdhui = LocalDate.parse(caseStartDate, dateFormat);
                        long totalYears = ChronoUnit.YEARS.between(dateAnniversaire, aujourdhui);
                        // age<=18 ans
                        if (totalYears<=18) {
                            // ajout des données dans la chaine de sortie au format JSON
                            data.append("{");
                            data.append("\"firstName\" : \"").append(JSONValue.escape(iPerson.getFirstName())).append("\",");
                            data.append("\"lastName\" : \"").append(JSONValue.escape(iPerson.getLastName())).append("\",");
                            data.append("\"age\" : \"").append(JSONValue.escape(String.valueOf(totalYears))).append("\"");
                            data.append("},");
                        }
                    }

                }
            }
        }

        if (data.charAt(data.length() - 1) == ',') data.delete(data.length() - 1, data.length());
        data.append("],");
        data.append("\"persons\" : [");

        // lit  les informations pour chaque personne
        for(Personn iPerson:personService.listAllPersonns()) {
            // même adresse que la valeur donnée en paramètre
            if(iPerson.getAddress().equals(address)) {
                // lit  les informations pour chaque medical record
                for (MedicalRecord iMedicalRecord : medicalRecordService.listAllMedicalRecords()) {
                    // même nom et même prénom que personne
                    if (iMedicalRecord.getLastName().equals(iPerson.getLastName()) & iMedicalRecord.getFirstName().equals(iPerson.getFirstName())) {
                        // calcul age de la personne
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        String myDate = iMedicalRecord.getBirthdate();
                        LocalDate dateAnniversaire = LocalDate.parse(myDate, dateFormat);
                        String caseStartDate = dateFormat.format(LocalDateTime.now());
                        LocalDate aujourdhui = LocalDate.parse(caseStartDate, dateFormat);
                        long totalYears = ChronoUnit.YEARS.between(dateAnniversaire, aujourdhui);
                        // age>18 ans
                        if (totalYears > 18) {
                            // ajout des données dans la chaine de sortie au format JSON
                            data.append("{");
                            data.append("\"firstName\" : \"").append(JSONValue.escape(iMedicalRecord.getFirstName())).append("\",");
                            data.append("\"lastName\" : \"").append(JSONValue.escape(iMedicalRecord.getLastName())).append("\"");
                            data.append("},");
                        }
                    }
                }

            }
        }
        if (data.charAt(data.length() - 1) == ',') data.delete(data.length() - 1, data.length());
        data.append("]");
        data.append("}");

        logger.info(new StringBuffer("Get list of childs"));
        return data;
    }

    public StringBuilder getPersonByStationAndCount(String station) throws ParseException {
        int childCounter=0;
        int adultCounter=0;
        StringBuilder data = new StringBuilder();
        data.append("{\"station\" : " + station + ",");
        data.append("\"persons\" : [");
        // lit  les informations pour chaque station
        for (FireStation iFireStation : fireStationService.listAllFireStations()) {
            // même numéro de station que la valeur donnée en paramètre
            if (iFireStation.getStation().equals(station)) {
                // lit  les informations pour chaque personne
                for (Personn iPersonn : personService.listAllPersonns()) {
                    // adresse personne=adresse station
                    if(iPersonn.getAddress().equals(iFireStation.getAddress())) {
                        // ajout des données dans la chaine de sortie au format JSON
                        data.append("{");
                        data.append("\"firstName\" : \"").append(JSONValue.escape(iPersonn.getFirstName())).append("\",");
                        data.append("\"lastName\" : \"").append(JSONValue.escape(iPersonn.getLastName())).append("\",");
                        data.append("\"address\" : \"").append(JSONValue.escape(iPersonn.getAddress())).append("\",");
                        data.append("\"phone\" : \"").append(JSONValue.escape(iPersonn.getPhone())).append("\"");
                        data.append("},");

                        // lit  les informations pour chaque medical record
                        for (MedicalRecord iMedicalRecord : medicalRecordService.listAllMedicalRecords()) {
                            // même nom et même prénom que personne
                            if (iMedicalRecord.getLastName().equals(iPersonn.getLastName()) & iMedicalRecord.getFirstName().equals(iPersonn.getFirstName())) {
                                // calcul age de la personne
                                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                                String myDate = iMedicalRecord.getBirthdate();
                                LocalDate dateAnniversaire = LocalDate.parse(myDate, dateFormat);
                                String caseStartDate = dateFormat.format(LocalDateTime.now());
                                LocalDate aujourdhui = LocalDate.parse(caseStartDate, dateFormat);
                                long totalYears = ChronoUnit.YEARS.between(dateAnniversaire, aujourdhui);
                                if(totalYears<=18) {
                                    // si <=18 ans ajoute 1 au compteur d'enfants
                                    childCounter+=1;
                                } else {
                                    // si >18 ans ajoute 1 au compteur d'adultes
                                    adultCounter+=1;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (data.charAt(data.length() - 1) == ',') data.delete(data.length() - 1, data.length());
        data.append("],");

        data.append("\"adultCount\" : ").append(adultCounter).append(",");
        data.append("\"childCount\" : ").append(childCounter);
        data.append("}");

        logger.info(new StringBuffer("Get list of persons by station and count adults and children"));

        return data;
    }

    public StringBuilder getPhoneForAFireStation(String station) throws ParseException {
        StringBuilder data = new StringBuilder();
        data.append("{\"station\" : " + station + ",");
        data.append("\"phones\" : [");
        // lit  les informations pour chaque station
        for (FireStation iFireStation : fireStationService.listAllFireStations()) {
            // même numéro de station que la valeur donnée en paramètre
            if (iFireStation.getStation().equals((String) station)) {
                // lit  les informations pour chaque personne
                for (Personn iPersonn : personService.listAllPersonns()) {
                    // adresse personne=adresse station
                    if (iPersonn.getAddress().equals(iFireStation.getAddress())) {
                        // ajout des données dans la chaine de sortie au format JSON
                        data.append("{");
                        data.append("\"phone\" : \"").append(JSONValue.escape(iPersonn.getPhone())).append("\"");
                        data.append("},");
                    }
                }
            }
        }

        if (data.charAt(data.length() - 1) == ',') data.delete(data.length() - 1, data.length());
        data.append("]");
        data.append("}");

        logger.info(new StringBuffer("Get list of phones number by station"));
        return data;
    }

    public StringBuilder getPersonLivedAtAddressWithFireStationNumber(String address) throws ParseException {
        StringBuilder data = new StringBuilder();
        data.append("{\"persons\" : [");
        // lit  les informations pour chaque personne
        for (Personn iPersonn : personService.listAllPersonns()) {
            // même adresse que la valeur donnée en paramètre
            if (iPersonn.getAddress().equals(address)) {
                // lit  les informations pour chaque station
                for (FireStation iFireStation : fireStationService.listAllFireStations()) {
                    // adresse station=adresse personne
                    if(iFireStation.getAddress().equals(iPersonn.getAddress())) {
                        // ajout des données dans la chaine de sortie au format JSON
                        data.append("{");
                        data.append("\"lastName\" : \"").append(JSONValue.escape(iPersonn.getLastName())).append("\",");
                        data.append("\"firstName\" : \"").append(JSONValue.escape(iPersonn.getFirstName())).append("\",");
                        data.append("\"phone\" : \"").append(JSONValue.escape(iPersonn.getPhone())).append("\",");
                        data.append("\"station\" : \"").append(JSONValue.escape(iFireStation.getStation())).append("\",");
                        // lit  les informations pour chaque medical record
                        for (MedicalRecord iMedicalRecord : medicalRecordService.listAllMedicalRecords()) {
                            // même nom et même prénom que personne
                            if (iMedicalRecord.getLastName().equals(iPersonn.getLastName()) & iMedicalRecord.getFirstName().equals(iPersonn.getFirstName())) {
                                //calcul age de la personne
                                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                                String myDate = iMedicalRecord.getBirthdate();
                                LocalDate dateAnniversaire = LocalDate.parse(myDate, dateFormat);
                                String caseStartDate = dateFormat.format(LocalDateTime.now());
                                LocalDate aujourdhui = LocalDate.parse(caseStartDate, dateFormat);
                                long totalYears = ChronoUnit.YEARS.between(dateAnniversaire, aujourdhui);

                                data.append("\"age\" : \"").append(JSONValue.escape(String.valueOf(totalYears))).append("\",");
                                data.append("\"medications\" : [");
                                // lecture de chaque médication
                                for(int i=0;i<iMedicalRecord.getMedications().size();i++) {
                                    String medication=iMedicalRecord.getMedications().get(i);
                                    // ajout des données médications dans la chaine de sortie au format JSON
                                    data.append("\"").append(JSONValue.escape(medication)).append("\",");
                                }
                                if (data.charAt(data.length() - 1) == ',') data.delete(data.length() - 1, data.length());
                                data.append("],");
                                data.append("\"allergies\" : [");
                                // lecture de chaque allergie
                                for(int i=0;i<iMedicalRecord.getAllergies().size();i++) {
                                    String allergie=iMedicalRecord.getAllergies().get(i);
                                    // ajout des données allergies dans la chaine de sortie au format JSON
                                    data.append("\"").append(JSONValue.escape(allergie)).append("\",");
                                }
                                if (data.charAt(data.length() - 1) == ',') data.delete(data.length() - 1, data.length());
                                data.append("]");
                                data.append("},");
                            }
                        }
                    }
                }
            }
        }
        if (data.charAt(data.length() - 1) == ',') data.delete(data.length() - 1, data.length());
        data.append("]");
        data.append("}");
        logger.info(new StringBuffer("Get list of persons live at an adress with station number"));
        return data;
    }

    public StringBuilder getInformationsForSeveralStations(String stations) {
        StringBuilder data = new StringBuilder();
        //ArrayList<String>ListStations=new ArrayList<>();
        //for(String subStr : Arrays.asList(stations.split("/"))) ListStations.add(subStr);
        List<String> ListStations = new ArrayList<String>(Arrays.asList(stations.split(",")));
        int personCounter=0;

        // Pour chaque élement de la liste des stations
        data.append("{\"stations\" : [");
        for(String iStation:ListStations) {
            data.append("{\"number\" : ").append(iStation).append(",");
            data.append("\"Homes\" : [");
            // lit  les informations pour chaque station
            for (FireStation iFireStation : fireStationService.listAllFireStations()) {
                if(iFireStation.getStation().equals(iStation)) {
                    data.append("{\"address\" : \"").append(JSONValue.escape(iFireStation.getAddress())).append("\",");
                    data.append("\"persons\" : [");
                    // lit  les informations pour chaque personne
                    for (Personn iPersonn : personService.listAllPersonns()) {
                        if(iPersonn.getAddress().equals(iFireStation.getAddress())) {
                            personCounter+=1;
                            // ajout des données dans la chaine de sortie au format JSON
                            data.append("{");
                            data.append("\"firstName\" : \"").append(JSONValue.escape(iPersonn.getFirstName())).append("\",");
                            data.append("\"lastName\" : \"").append(JSONValue.escape(iPersonn.getLastName())).append("\",");
                            data.append("\"phone\" : \"").append(JSONValue.escape(iPersonn.getPhone())).append("\",");
                            // lit  les informations pour chaque medical record
                            for (MedicalRecord iMedicalRecord : medicalRecordService.listAllMedicalRecords()) {
                                // même nom et même prénom que personne
                                if (iMedicalRecord.getLastName().equals(iPersonn.getLastName()) & iMedicalRecord.getFirstName().equals(iPersonn.getFirstName())) {
                                    //calcul age de la personne
                                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                                    String myDate = iMedicalRecord.getBirthdate();
                                    LocalDate dateAnniversaire = LocalDate.parse(myDate, dateFormat);
                                    String caseStartDate = dateFormat.format(LocalDateTime.now());
                                    LocalDate aujourdhui = LocalDate.parse(caseStartDate, dateFormat);
                                    long totalYears = ChronoUnit.YEARS.between(dateAnniversaire, aujourdhui);

                                    data.append("\"age\" : \"").append(JSONValue.escape(String.valueOf(totalYears))).append("\",");

                                    data.append("\"medicalRecords\" : {");
                                    data.append("\"medications\" : [");
                                    // lecture de chaque médication
                                    for (int i = 0; i < iMedicalRecord.getMedications().size(); i++) {
                                        String medication = iMedicalRecord.getMedications().get(i);
                                        // ajout des données médications dans la chaine de sortie au format JSON
                                        data.append("\"").append(JSONValue.escape(medication)).append("\",");
                                    }
                                    if (data.charAt(data.length() - 1) == ',')
                                        data.delete(data.length() - 1, data.length());
                                    data.append("],");
                                    data.append("\"allergies\" : [");
                                    // lecture de chaque allergie
                                    for (int i = 0; i < iMedicalRecord.getAllergies().size(); i++) {
                                        String allergie = iMedicalRecord.getAllergies().get(i);
                                        // ajout des données allergies dans la chaine de sortie au format JSON
                                        data.append("\"").append(JSONValue.escape(allergie)).append("\",");
                                    }
                                    if (data.charAt(data.length() - 1) == ',')
                                        data.delete(data.length() - 1, data.length());
                                    data.append("]}},");
                                }

                            }

                        }

                    }
                    if (data.charAt(data.length() - 1) == ',')
                        data.delete(data.length() - 1, data.length());
                    data.append("]},");
                }
            }
            if (data.charAt(data.length() - 1) == ',')
                data.delete(data.length() - 1, data.length());
            data.append("]},");
        }

        if (data.charAt(data.length() - 1) == ',') data.delete(data.length() - 1, data.length());
        data.append("]}");
        logger.info(new StringBuffer("Give information about several stations"));
        return data;

    }

    public StringBuilder getInfosByName(String firstName,String lastName) {
        StringBuilder data = new StringBuilder();
        data.append("{\"persons\" : [");
        // lit  les informations pour chaque personne
        for (Personn iPersonn : personService.listAllPersonns()) {
            // même nom et prénom que les valeurs données en paramètre
            if (iPersonn.getFirstName().equals(firstName) & iPersonn.getLastName().equals(lastName)) {
                // ajout des données dans la chaine de sortie au format JSON
                data.append("{");
                data.append("\"lastName\" : \"").append(JSONValue.escape(iPersonn.getLastName())).append("\",");
                data.append("\"firstName\" : \"").append(JSONValue.escape(iPersonn.getFirstName())).append("\",");
                data.append("\"email\" : \"").append(JSONValue.escape(iPersonn.getEmail())).append("\",");

                for (MedicalRecord iMedicalRecord : medicalRecordService.listAllMedicalRecords()) {
                    // même nom et même prénom que personne
                    if (iMedicalRecord.getLastName().equals(iPersonn.getLastName()) & iMedicalRecord.getFirstName().equals(iPersonn.getFirstName())) {
                        //calcul age de la personne
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        String myDate = iMedicalRecord.getBirthdate();
                        LocalDate dateAnniversaire = LocalDate.parse(myDate, dateFormat);
                        String caseStartDate = dateFormat.format(LocalDateTime.now());
                        LocalDate aujourdhui = LocalDate.parse(caseStartDate, dateFormat);
                        long totalYears = ChronoUnit.YEARS.between(dateAnniversaire, aujourdhui);

                        data.append("\"age\" : \"").append(JSONValue.escape(String.valueOf(totalYears))).append("\",");

                        data.append("\"medicalRecords\" : {");
                        data.append("\"medications\" : [");
                        // lecture de chaque médication
                        for (int i = 0; i < iMedicalRecord.getMedications().size(); i++) {
                            String medication = iMedicalRecord.getMedications().get(i);
                            // ajout des données médications dans la chaine de sortie au format JSON
                            data.append("\"").append(JSONValue.escape(medication)).append("\",");
                        }
                        if (data.charAt(data.length() - 1) == ',')
                            data.delete(data.length() - 1, data.length());
                        data.append("],");
                        data.append("\"allergies\" : [");
                        // lecture de chaque allergie
                        for (int i = 0; i < iMedicalRecord.getAllergies().size(); i++) {
                            String allergie = iMedicalRecord.getAllergies().get(i);
                            // ajout des données allergies dans la chaine de sortie au format JSON
                            data.append("\"").append(JSONValue.escape(allergie)).append("\",");
                        }
                        if (data.charAt(data.length() - 1) == ',')
                            data.delete(data.length() - 1, data.length());
                        data.append("]}},");
                    }
                }
            }
        }
        if (data.charAt(data.length() - 1) == ',')
            data.delete(data.length() - 1, data.length());
        data.append("]");
        data.append("}");
        logger.info(new StringBuffer("Get information on a person by lastname and firstname"));
        return data;
    }

public StringBuilder getAllEmailForACity(String city) {
    StringBuilder data = new StringBuilder();
    data.append("{\"emails\" : [");
    // lit  les informations pour chaque personne
    for (Personn iPersonn : personService.listAllPersonns()) {
        // même ville
        if(iPersonn.getCity().equals(city)) {
            data.append("{");
            data.append("\"email\" : \"").append(JSONValue.escape(iPersonn.getEmail())).append("\"");
            data.append("},");
        }
    }
    if (data.charAt(data.length() - 1) == ',')
        data.delete(data.length() - 1, data.length());
    data.append("]");
    data.append("}");
    logger.info(new StringBuffer("Get list of mails for a city"));
    return data;
}
}
