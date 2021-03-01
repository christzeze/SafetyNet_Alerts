package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.PersonInfosFull;
import com.openclassroms.SafetyNetAlerts.repository.PersonFullRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonInfoFullServiceImpl implements PersonInfoFullService{
    private PersonFullRepository personFullRepository;

    public Iterable<PersonInfosFull> getlistInfoFullPerson() {
        return personFullRepository.findAll();
    }
}
