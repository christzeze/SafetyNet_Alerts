package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import java.util.List;

@Data
public class FireStationCoverage {
    List<PersonInfosFull> fireStationPersons;
    int adultCount;
    int childCount;
}
