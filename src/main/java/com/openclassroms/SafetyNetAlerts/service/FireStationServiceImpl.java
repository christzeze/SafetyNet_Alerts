package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.*;
import com.openclassroms.SafetyNetAlerts.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FireStationServiceImpl implements FireStationService {

    private FireStationRepository fireStationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PersonServiceImpl personService;

    @Autowired
    private CalculateAgeService calculateAgeService;

    @Autowired
    private PersonFullRepository personFullRepository;

    //@Autowired
    //private FireStationRepositoryImpl fireStationRepositoryImpl;



    /**
     * Constructeur
     */
    public FireStationServiceImpl(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

    /**
            * Logger
     */
    private static final Logger logger = LogManager.getLogger("PersonServiceImpl");

    /**
     * Liste des stations incendie dans la base
     */
    public Iterable<FireStation> listAllFireStations() {
        return fireStationRepository.findAll();
    }

    /**
     * Sauvegarde d'une station incendie dans la base
     */
    public FireStation save(FireStation fireStation) {
        return fireStationRepository.save(fireStation);
    }

    /**
     * Sauvegarde de toutes les stations incendie dans la base
     */
    public Iterable<FireStation> save(List<FireStation> fireStations) {
        return fireStationRepository.saveAll(fireStations);

    }

    /**
     * Suppression d'une fire station par numéro de station
     */
    @Override
    public void deleteFireStationByStationNumber(int station) {
        List<FireStation> fireStations=fireStationRepository.findStationByStation(station);
        for(FireStation fireStation:fireStations) {
            fireStationRepository.delete(fireStation);
        }
    }

    /**
     * Suppression d'une fire station par adresse
     */
    @Override
    public ResponseEntity deleteFireStationByAddress(String address) {
        List<FireStation> fireStations=fireStationRepository.findStationByAddress(address);
        for(FireStation fireStation:fireStations) {
            fireStationRepository.delete(fireStation);
        }
        if(fireStations!=null) {
            logger.info("delete fire station succeeded");
            return ResponseEntity.ok(fireStationRepository);
        } else {
            return ResponseEntity.badRequest()
                    .body(fireStationRepository);
        }
    }

    /**
     * mise à jour d'une fire station par adresse
     */
    @Override
    public ResponseEntity updateFireStation(String address, FireStation fireStationDetails) {

        List<FireStation> fireStations=fireStationRepository.findStationByAddress(address);
        for(FireStation fireStation:fireStations) {
            fireStation.setStation(fireStationDetails.getStation());
            fireStation.setAddress(fireStationDetails.getAddress());
            final FireStation updateFireStation=fireStationRepository.save(fireStation);
        }
        if(fireStations!=null) {
            logger.info("Update fire station succeeded");
            return ResponseEntity.ok(fireStationRepository);
        } else {
            return ResponseEntity.badRequest()
                    .body(fireStationRepository);
        }
    }

    /**
     * Liste des personnes couvertes par une caserne de pompiers
     * @return
     */
    @Override
    public FireStationCoverage getFireStationCoverage(int station) {
        int childCounter=0;
        int adultCounter=0;

        FireStationCoverage fireStationCoverage=new FireStationCoverage();
        List<PersonInfosFull> fireStationPersons = new ArrayList<>();
        List<Person> personByStation = new ArrayList<>();

        List<FireStation> addressCoveredByStation=fireStationRepository.findStationByStation(station);

        for (FireStation a:addressCoveredByStation) {
            List<Person> personByAddress = personService.findPersonByAddress(a.getAddress());
            personByStation.addAll(personByAddress);
        }
        for(Person person:personByStation) {
                PersonInfosFull infoPersonFull = personService.getFullInformationPerson(person);
                MedicalRecord medicalRecord=medicalRecordRepository.findFirstMedicalRecordByIdPerson(person.getId());
                int age=calculateAgeService.calculateAge(medicalRecord.getBirthdate());
                if(calculateAgeService.isChild(medicalRecord)) {
                    childCounter += 1;
                } else {
                    adultCounter += 1;
                }
            fireStationPersons.add(infoPersonFull);
            }

        fireStationCoverage.setFireStationPersons(fireStationPersons);
        fireStationCoverage.setChildCount(childCounter);
        fireStationCoverage.setAdultCount(adultCounter);

        logger.info("List of coverage by fire station create");

        return fireStationCoverage;
    }

    /**
     * Liste des numéros de téléphones des personnes desservies par une caserne de pompiers
     * @return
     */
    @Override
    public List<PhoneCoverage> getPhoneNumbersByStation(int station) {

        List<PhoneCoverage>listOfPerson=new ArrayList<>();
        boolean isAvailable=false;


        List<FireStation> coveredStation=fireStationRepository.findStationByStation(station);

        for(FireStation fireStation:coveredStation) {

            Sort sortKey = Sort.by("address");
            List<Person> personByAddress = personRepository.findPersonByAddress(fireStation.getAddress(), sortKey);

            for (Person person : personByAddress) {
                PhoneCoverage phoneCoverage = new PhoneCoverage();
                phoneCoverage.setPhone(person.getPhone());
                for(int i=0;i<listOfPerson.size();i++) {
                    if(person.getPhone().equals(listOfPerson.get(i).getPhone())) {
                        isAvailable=true;
                    }
                }
                if(!isAvailable) {
                    listOfPerson.add(phoneCoverage);
                }
                isAvailable=false;

            }

        }
        logger.info("List of phone numbers by station create");
        return listOfPerson;
    }

    /**
     * Liste des foyers desservis par une liste de casernes de pompiers
     * @return
     */
    @Override
    public List<FireStationsFlood> getCoverageFireStationForSeveralFireStations(String stations) {
        String myStations=String.valueOf(stations);
        List<String> ListStations = new ArrayList<String>(Arrays.asList(myStations.split(",")));
        List<FireStationsFlood>listOfPerson=new ArrayList<>();

        for (String station : ListStations) {

            List<String> listStations2=new ArrayList<>();
            int stationNumber=Integer.parseInt(station);
            List<FireStation> coveredStation = fireStationRepository.findStationByStation(stationNumber);
            for (FireStation fireStation : coveredStation) {
                List<Person> personByAddress = personRepository.findPersonByAddress(fireStation.getAddress(),Sort.by("address"));
                for (Person person : personByAddress) {
                    MedicalRecord medicalRecord=medicalRecordRepository.findFirstMedicalRecordByIdPerson(person.getId());
                    //MedicalRecord medicalRecord=medicalRecordRepository.findFirstMedicalRecordByFirstNameAndLastName(person.getFirstName(),person.getLastName());
                    FireStationsFlood fireStationsFlood=new FireStationsFlood();
                    fireStationsFlood.setFirstName(person.getFirstName());
                    fireStationsFlood.setLastName(person.getLastName());
                    fireStationsFlood.setAddress(person.getAddress());
                    fireStationsFlood.setPhone(person.getPhone());
                    int age=calculateAgeService.calculateAge(medicalRecord.getBirthdate());
                    fireStationsFlood.setAge(age);
                    fireStationsFlood.setMedications(medicalRecord.getMedications());
                    fireStationsFlood.setAllergies(medicalRecord.getAllergies());
                    for (FireStation a:coveredStation) {
                        if (!listStations2.contains(String.valueOf(a.getStation()))) {
                            listStations2.add(String.valueOf(a.getStation()));
                        }
                    }
                    fireStationsFlood.setStation(listStations2);
                    listOfPerson.add(fireStationsFlood);
                }
            }
        }
        logger.info("List of persons by a list of station create");

        return listOfPerson;
    }


}