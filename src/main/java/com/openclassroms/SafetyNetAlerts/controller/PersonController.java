package com.openclassroms.SafetyNetAlerts.controller;

import com.openclassroms.SafetyNetAlerts.dto.PersonInfos;
import com.openclassroms.SafetyNetAlerts.model.*;
import com.openclassroms.SafetyNetAlerts.service.PersonService;
import com.openclassroms.SafetyNetAlerts.service.PersonServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
public class PersonController {

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("PersonServiceImpl");

    private PersonService personService;


    private PersonController(PersonServiceImpl personService) {
        this.personService = personService;
    }

    //liste de toutes les personnes
    @GetMapping("/listPersons")
    public Iterable<Person> list() {
        return personService.listAllPersonns();
    }

    // Informations sur une personne
    @GetMapping("/personInfo")
    public List<PersonInfos> listofPersonByFirstNameAndLastName(@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName) {
        return personService.getlistPersonsByFirstNameAndLastName(firstName, lastName);
    }

    // Liste enfants+autres personnes dans le foyer
    @GetMapping("/childAlert")
    public List<Child> listofChilds(@RequestParam(required = false) String address) throws ParseException {
        return personService.getlistOfChildren(address);
    }

    //liste des habitants vivant à l’adresse donnée ainsi que le numéro de la caserne
    //de pompiers la desservant
    @GetMapping("/fire")
    public List<Person> listFull(@RequestParam(required = false) String address) {
        return personService.getAllInformationsForPersonnAtAnAddress(address);
    }

    // adresses mail de tous les habitants de la ville
    @GetMapping("/communityEmail")
    public List<Email> listofEmailByCity(@RequestParam(required = false) String city) {
        return personService.getEmailPerCity(city);
    }

    // add a person
    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person abstractPerson) {
        return ResponseEntity.ok(personService.save(abstractPerson));
    }

    //delete a person
    @DeleteMapping("/person")
    public void removePerson(@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName) {
        personService.deletePerson(firstName, lastName);
        logger.info("Remove person(s) and associated medical records succeeded");

    }

    // update a person
    @PutMapping("/person")
    public void UpdatePerson(@RequestBody Person abstractPersonDetails, @RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName) {
        personService.updatePerson(firstName, lastName, abstractPersonDetails);
    }

}



