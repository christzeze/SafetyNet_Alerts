package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.Personn;
import com.openclassroms.SafetyNetAlerts.repository.PersonnRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private PersonnRepository personnRepository;

    /**
     * Constructeur
     */
    public PersonService(PersonnRepository personnRepository ) {
        this.personnRepository=personnRepository;
    }


    /**
     * Liste des personnes dans la base
     */
    public Iterable<Personn> listAllPersonns() {
        return personnRepository.findAll();
    }

    /**
     * Sauvegarde d'une personne dans la base
     */
    public Personn save(Personn personn) {
        return personnRepository.save(personn);
    }

    /**
     * Sauvegarde de toutes les personnes dans la base
     */
    public Iterable<Personn> save(List<Personn> personns) {
        return personnRepository.saveAll(personns);

    }
}
