package com.openclassroms.SafetyNetAlerts.controller;

import com.openclassroms.SafetyNetAlerts.model.FireStation;
import com.openclassroms.SafetyNetAlerts.model.Personn;
import com.openclassroms.SafetyNetAlerts.service.FireStationService;
import com.openclassroms.SafetyNetAlerts.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireStationController {
    private FireStationService fireStationService;

    private FireStationController(FireStationService fireStationService) {
        this.fireStationService=fireStationService;
    }

    @GetMapping("/FireStations")
    public Iterable<FireStation> list() {
        return fireStationService.listAllFireStations();
    }
}
