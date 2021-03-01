package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.*;
import com.openclassroms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassroms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassroms.SafetyNetAlerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private CalculateAgeService calculateAgeService;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private FireStationService fireStationService;

    @Autowired
    private FireStationServiceImpl fireStationServiceImpl;
    @Autowired
    private MedicalRecordServiceImpl medicalRecordServiceImpl2;


    /**
     * Constructeur
     */
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("PersonServiceImpl");

    /**
     * Liste des personnes dans la base
     */
    @Override
    public Iterable<Person> listAllPersonns() {
        return personRepository.findAll();
    }

    /**
     * Sauvegarde d'une personne dans la base
     */
    @Override
    public Person save(Person person) {
        Person savedPerson = personRepository.save(person);
        logger.info("Person saved {}", savedPerson);
        return savedPerson;
    }

    /**
     * Sauvegarde de toutes les personnes dans la base
     */
    @Override
    public Iterable<Person> save(List<Person> people) {
        return personRepository.saveAll(people);

    }

    @Override
    public ResponseEntity deletePerson(String firstName, String lastName) {

        List<Person> persons = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
        for(Person person:persons){
            MedicalRecord medicalRecord=medicalRecordRepository.findFirstMedicalRecordByIdPerson(person.getId());
            medicalRecordRepository.delete(medicalRecord);
        }
        for(Person person:persons) {
            personRepository.delete(person);
        }
        logger.info("Update  person(s) succeeded");
        if (persons!=null) {
            logger.info("person deleted");
            return ResponseEntity.ok(personRepository);
        }else{
            logger.info("person not deleted");
            return ResponseEntity.badRequest()
                    .body(personRepository);
        }

    }

    @Override
    public ResponseEntity updatePerson(String firstName, String lastName, Person personDetails) {
        List<Person> persons=personRepository.findPersonByFirstNameAndLastName(firstName,lastName);
        Person updatePerson=null;
        for(Person person:persons) {
            person.setPhone(personDetails.getPhone());
            person.setAddress(personDetails.getAddress());
            person.setCity(personDetails.getCity());
            person.setEmail(personDetails.getEmail());
            person.setZip(personDetails.getZip());
            updatePerson=personRepository.save(person);
        }
        if (updatePerson!=null) {
            logger.info("person update");
            return ResponseEntity.ok(personRepository);
        }else{
            logger.info("person not update");
            return ResponseEntity.badRequest()
                    .body(personRepository);
        }
    }


    @Override
    public List<Person> findPersonByAddress(String address) {
        Sort sortKey = Sort.by("address");
        return personRepository.findPersonByAddress(address, sortKey);
    }

    /**
     * Liste des personnes selon le nom et le prénom
     */
    @Override
    public List<PersonInfos> getlistPersonsByFirstNameAndLastName(String firstName, String lastName) {

        List<PersonInfos> listPersonsInfo = new ArrayList<>();
        List<String> listStations = new ArrayList<>();

        List<Person> persons = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);

        for (Person person : persons) {
            MedicalRecord medicalRecord=medicalRecordRepository.findFirstMedicalRecordByIdPerson(person.getId());

            List<FireStation> stations = fireStationRepository.findStationByAddress(person.getAddress());

            if(medicalRecord==null) {
                logger.error("Person " + person + " don't exist");
            }

            int age=calculateAgeService.calculateAge(medicalRecord.getBirthdate());

            PersonInfos personInfos = new PersonInfos();
            personInfos.setFirstName(person.getFirstName());
            personInfos.setLastName(person.getLastName());
            personInfos.setAddress(person.getAddress());
            personInfos.setEmail(person.getEmail());
            personInfos.setPhone(person.getPhone());
            personInfos.setAge(age);

            for (FireStation a : stations) {
                listStations.add(String.valueOf(a.getStation()));
            }

            personInfos.setStation(listStations);
            personInfos.setAllergies(medicalRecord.getAllergies());
            personInfos.setMedications(medicalRecord.getMedications());
            listPersonsInfo.add(personInfos);
            logger.info("Add person to list of persons by name");

        }


        logger.info("List of persons by first name and last name create");
        return listPersonsInfo;
    }

    public List<Child> getlistOfChildren(String address) {
        List<Child> children = new ArrayList<>();
        List<Person> adults = new ArrayList<>();
        //int age = 0;

        Sort sortKey = Sort.by("address");
        List<Person> persons = personRepository.findPersonByAddress(address, sortKey);

        for (Person person : persons) {
            MedicalRecord medicalRecord=medicalRecordRepository.findFirstMedicalRecordByIdPerson(person.getId());
            //MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            if (medicalRecord == null) {
                logger.error("Person " + person + " don't exist");
            }
            if (calculateAgeService.isAdult(medicalRecord)) {
                adults.add(person);
            } else {
                int age=calculateAgeService.calculateAge(medicalRecord.getBirthdate());
                Child child = new Child();
                child.setFirstName(person.getFirstName());
                child.setLastName(person.getLastName());
                child.setAge(age);
                child.setHouseMembers(adults);
                children.add(child);
                logger.info("Add child and adults to list of persons");
            }
        }
        logger.info("List of childs and adults create");
        return children;
    }

    public List<PersonInfosFull> getAllInformationsForPersonnAtAnAddress(String address) {
        List<String> allergies = null;
        List<String> medications = null;
        List<PersonInfosFull> listPersonsInfo = new ArrayList<>();
        List<String> listStations = new ArrayList<>();

        Sort sortKey = Sort.by("address");
        List<Person> persons = personRepository.findPersonByAddress(address, sortKey);


        for (Person person : persons) {
            MedicalRecord medicalRecord=medicalRecordRepository.findFirstMedicalRecordByIdPerson(person.getId());
            //MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            List<FireStation> fireStations = fireStationRepository.findStationByAddress(person.getAddress());


            allergies = medicalRecord.getAllergies();
            medications = medicalRecord.getMedications();
            int age=calculateAgeService.calculateAge(medicalRecord.getBirthdate());


            PersonInfosFull personInfosFull = new PersonInfosFull();

            personInfosFull.setFirstName(person.getFirstName());
            personInfosFull.setLastName(person.getLastName());
            personInfosFull.setAddress(person.getAddress());
            personInfosFull.setEmail(person.getEmail());
            personInfosFull.setPhone(person.getPhone());


            for (FireStation a : fireStations) {
                if (!listStations.contains(String.valueOf(a.getStation()))) {
                    listStations.add(String.valueOf(a.getStation()));
                }
            }
            personInfosFull.setStation(listStations);


            personInfosFull.setAge(age);
            personInfosFull.setAllergies(allergies);
            personInfosFull.setMedications(medications);


            listPersonsInfo.add(personInfosFull);
            logger.info("Add full infos to list of persons");
        }
        logger.info("List of full infos create");
        return listPersonsInfo;
    }


    public PersonInfosFull getFullInformationPerson(Person person) {
        List<String> listStations = new ArrayList<>();

        PersonInfosFull result = new PersonInfosFull();
        result.setFirstName(person.getFirstName());
        result.setLastName(person.getLastName());
        result.setAddress(person.getAddress());
        result.setPhone(person.getPhone());
        result.setEmail(person.getEmail());
        //FireStation station=fireStationRepository.findStationByAddressForFireStation(person.getAddress());
        List<FireStation> fireStations = fireStationRepository.findStationByAddress(person.getAddress());

        for (FireStation a : fireStations) {
            if (!listStations.contains(String.valueOf(a.getStation()))) {
                listStations.add(String.valueOf(a.getStation()));
            }
        }
        result.setStation(listStations);
        //result.setStation(fireStationServiceImpl.stationNumber(person.getAddress()));

        //MedicalRecord medicalRecord = medicalRecordServiceImpl2.getMedicalRecords(person.getFirstName(),person.getLastName());
        //List<MedicalRecord> medicalRecordList = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        //MedicalRecord medicalRecord= medicalRecordRepository.findFirstMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        MedicalRecord medicalRecord=medicalRecordRepository.findFirstMedicalRecordByIdPerson(person.getId());
        if (medicalRecord != null) {
            result.setAge(calculateAgeService.calculateAge(medicalRecord.getBirthdate()));
            //for (MedicalRecord medicalRecord : medicalRecordList) {
            result.setAllergies(medicalRecord.getAllergies());
            result.setMedications(medicalRecord.getMedications());
            //}
        }

        return result;
    }

    /**
     * Liste des emails par ville
     *
     * @return
     */
    @Override
    public List<Email> getEmailPerCity(String city) {
        List<Email> listOfPerson = new ArrayList<>();
        Iterable<Person> persons = personRepository.findPersonByCity(city);
        for (Person person : persons) {
            Email email = new Email();
            email.setEmail(person.getEmail());
            email.setCity(person.getCity());
            listOfPerson.add(email);
        }
        logger.info("List of emails create");
        return listOfPerson;
    }
}
