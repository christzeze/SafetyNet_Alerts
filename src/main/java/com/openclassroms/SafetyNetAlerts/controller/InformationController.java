package com.openclassroms.SafetyNetAlerts.controller;

import com.openclassroms.SafetyNetAlerts.model.Personn;
import com.openclassroms.SafetyNetAlerts.service.InformationService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Singleton;

@RestController



public class InformationController {
    private InformationService informationService;

    public InformationController(InformationService informationService) {
        this.informationService = informationService;
    }

    @GetMapping("/fireStation")
    public StringBuilder listofPersonByStationAndCount(@RequestParam(required = false) String stationNumber) throws ParseException {
        return informationService.getPersonByStationAndCount(stationNumber);
    }

    @GetMapping("/childAlert")
    public StringBuilder listofChilds(@RequestParam(required = false) String address) throws ParseException {
        return informationService.getListOfChild(address);
    }

    @GetMapping("/phoneAlert")
    public StringBuilder listofPhones(@RequestParam(required = false) String stationNumber) throws ParseException {
        return informationService.getPhoneForAFireStation(stationNumber);
    }

    @GetMapping("/fire")
    public StringBuilder listofPersonWithPhoneMedicationsAndAllergies(@RequestParam(required = false) String address) throws ParseException {
        return informationService.getPersonLivedAtAddressWithFireStationNumber(address);
    }

    @GetMapping("/flood/stations")
    public StringBuilder listofPersonForSeveralStations(@RequestParam(required = false) String stations) throws ParseException {
        return informationService.getInformationsForSeveralStations(stations);
    }

    @GetMapping("/personInfo")
    public StringBuilder listofPersonForSeveralStations(@RequestParam(required = false) String firstName,@RequestParam(required = false) String lastName) throws ParseException {
        return informationService.getInfosByName(firstName,lastName);
    }

    @GetMapping("/communityEmail")
    public StringBuilder listofEmailByCity(@RequestParam(required = false) String city) throws ParseException {
        return informationService.getAllEmailForACity(city);
    }
}
