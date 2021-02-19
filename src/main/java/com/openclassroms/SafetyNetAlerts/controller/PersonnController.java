package com.openclassroms.SafetyNetAlerts.controller;

import com.openclassroms.SafetyNetAlerts.model.Personn;
import com.openclassroms.SafetyNetAlerts.repository.PersonnRepository;
import com.openclassroms.SafetyNetAlerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController


public class PersonnController {
    private PersonService personService;

    private PersonnController(PersonService personService) {
        this.personService=personService;
    }

    @GetMapping("/listPersons")
    public Iterable<Personn> list() {
        return personService.listAllPersonns();
    }


    }

