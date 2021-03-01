package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.dto.PersonInfos;
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
    public Person save(Person Person) {
        Person savedPerson = personRepository.save(Person);
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

        List<Person> abstractPeople = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
        for (Person Person : abstractPeople) {
            MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByPersonId(Person.getId());
            medicalRecordRepository.delete(medicalRecord);
        }
        for (Person Person : abstractPeople) {
            personRepository.delete(Person);
        }
        logger.info("Update  person(s) succeeded");
        if (abstractPeople != null) {
            logger.info("person deleted");
            return ResponseEntity.ok(personRepository);
        } else {
            logger.info("person not deleted");
            return ResponseEntity.badRequest()
                    .body(personRepository);
        }

    }

    @Override
    public ResponseEntity updatePerson(String firstName, String lastName, Person PersonDetails) {
        List<Person> abstractPeople = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
        Person updatePerson = null;
        for (Person Person : abstractPeople) {
            Person.setPhone(PersonDetails.getPhone());
            Person.setAddress(PersonDetails.getAddress());
            Person.setCity(PersonDetails.getCity());
            Person.setEmail(PersonDetails.getEmail());
            Person.setZip(PersonDetails.getZip());
            updatePerson = personRepository.save(Person);
        }
        if (updatePerson != null) {
            logger.info("person update");
            return ResponseEntity.ok(personRepository);
        } else {
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

        List<Person> abstractPeople = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);

        for (Person Person : abstractPeople) {
            MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByPersonId(Person.getId());

            List<FireStation> stations = fireStationRepository.findStationByAddress(Person.getAddress());

            if (medicalRecord == null) {
                logger.error("Person " + Person + " don't exist");
            }

            int age = calculateAgeService.calculateAge(medicalRecord.getBirthdate());

            PersonInfos personInfos = new PersonInfos();
            personInfos.setFirstName(Person.getFirstName());
            personInfos.setLastName(Person.getLastName());
            personInfos.setAddress(Person.getAddress());
            personInfos.setEmail(Person.getEmail());
            personInfos.setPhone(Person.getPhone());
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
        List<Person> abstractPeople = personRepository.findPersonByAddress(address, sortKey);

        for (Person Person : abstractPeople) {
            MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByPersonId(Person.getId());
            //MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            if (medicalRecord == null) {
                logger.error("Person " + Person + " don't exist");
            }
            if (calculateAgeService.isAdult(medicalRecord)) {
                adults.add(Person);
            } else {
                int age = calculateAgeService.calculateAge(medicalRecord.getBirthdate());
                Child child = new Child();
                child.setFirstName(Person.getFirstName());
                child.setLastName(Person.getLastName());
                child.setAge(age);
                child.setHouseMembers(adults);
                children.add(child);
                logger.info("Add child and adults to list of persons");
            }
        }
        logger.info("List of childs and adults create");
        return children;
    }

    public List<Person> getAllInformationsForPersonnAtAnAddress(String address) {
        List<String> allergies = null;
        List<String> medications = null;
        List<Person> listPersonsInfo = new ArrayList<>();
        List<String> listStations = new ArrayList<>();

        Sort sortKey = Sort.by("address");
        List<Person> abstractPeople = personRepository.findPersonByAddress(address, sortKey);


        for (Person Person : abstractPeople) {
            MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByPersonId(Person.getId());
            //MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            List<FireStation> fireStations = fireStationRepository.findStationByAddress(Person.getAddress());


            allergies = medicalRecord.getAllergies();
            medications = medicalRecord.getMedications();
            int age = calculateAgeService.calculateAge(medicalRecord.getBirthdate());


            Person personInfosFull = new Person();

            personInfosFull.setFirstName(Person.getFirstName());
            personInfosFull.setLastName(Person.getLastName());
            personInfosFull.setAddress(Person.getAddress());
            personInfosFull.setEmail(Person.getEmail());
            personInfosFull.setPhone(Person.getPhone());


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


    public Person getFullInformationPerson(Person Person) {
        List<String> listStations = new ArrayList<>();

        Person result = new Person();
        result.setFirstName(Person.getFirstName());
        result.setLastName(Person.getLastName());
        result.setAddress(Person.getAddress());
        result.setPhone(Person.getPhone());
        result.setEmail(Person.getEmail());
        //FireStation station=fireStationRepository.findStationByAddressForFireStation(person.getAddress());
        List<FireStation> fireStations = fireStationRepository.findStationByAddress(Person.getAddress());

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
        MedicalRecord medicalRecord = medicalRecordRepository.findFirstMedicalRecordByPersonId(Person.getId());
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
        for (Person Person : persons) {
            Email email = new Email();
            email.setEmail(Person.getEmail());
            email.setCity(Person.getCity());
            listOfPerson.add(email);
        }
        logger.info("List of emails create");
        return listOfPerson;
    }
}
