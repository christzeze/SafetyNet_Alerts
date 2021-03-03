package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FireStationService {

    public Iterable<com.openclassroms.SafetyNetAlerts.model.FireStation> listAllFireStations();

    public com.openclassroms.SafetyNetAlerts.model.FireStation save(com.openclassroms.SafetyNetAlerts.model.FireStation fireStation);

    public Iterable<com.openclassroms.SafetyNetAlerts.model.FireStation> save(List<com.openclassroms.SafetyNetAlerts.model.FireStation> fireStations);

    public FireStationCoverage getFireStationCoverage(int station);

    public List<PhoneCoverage> getPhoneNumbersByStation(int station);

    public List<FireStationsFlood> getCoverageFireStationForSeveralFireStations(String stations);

    public void deleteFireStationByStationNumber(int station);

    public ResponseEntity deleteFireStationByAddress(String address);

    public ResponseEntity updateFireStation(String station, FireStation fireStationDetails);




}
