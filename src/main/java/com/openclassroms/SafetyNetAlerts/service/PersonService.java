package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.*;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

public interface PersonService {

    Iterable<Person> listAllPersonns();

    public Person save(Person person);

    public Iterable<Person> save(List<Person> people);


    public List<PersonInfos> getlistPersonsByFirstNameAndLastName(String firstName, String lastName);

    public List<Child> getlistOfChildren(String address) throws ParseException;

    public List<PersonInfosFull> getAllInformationsForPersonnAtAnAddress(String address);

    public List<Person> findPersonByAddress(String address);

    public List<Email> getEmailPerCity(String city);

    public ResponseEntity deletePerson(String firstName,String lastName);

    public ResponseEntity updatePerson(String firstName, String lastName, Person personDetails);
}
