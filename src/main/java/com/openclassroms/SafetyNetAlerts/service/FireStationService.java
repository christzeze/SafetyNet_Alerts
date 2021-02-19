package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.FireStation;
import com.openclassroms.SafetyNetAlerts.repository.FireStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireStationService {

    private FireStationRepository fireStationRepository;

    /**
     * Constructeur
     */
    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

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

}
