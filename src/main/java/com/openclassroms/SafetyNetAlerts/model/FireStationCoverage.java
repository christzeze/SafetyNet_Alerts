package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import java.util.List;

@Data
public class FireStationCoverage {
    List<Person> fireStationPersons;
    int adultCount;
    int childCount;
}
