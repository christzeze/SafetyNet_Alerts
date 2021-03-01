package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.model.*;
import com.openclassroms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassroms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassroms.SafetyNetAlerts.repository.PersonRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc

public class FireStationServiceTest {
    private List<Person> persons;
    private List<FireStation> stations;
    private List<MedicalRecord> medicalRecords;

    @Autowired
    PersonServiceImpl personServiceImpl;

    @Autowired
    CalculateAgeService calculateAgeService;

    @Autowired
    FireStationServiceImpl fireStationServiceImpl;

    @MockBean
    private MedicalRecordServiceImpl medicalRecordServiceImpl2;


    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private FireStationRepository fireStationRepository;

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;



    List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
    List<String> allergies = Arrays.asList("nillacilan");

    Person johnBoyd = new Person(1, "John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
    Person jacobBoyd = new Person(2, "Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
    Person tenleyBoyd = new Person(3, "Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
    Person rogerBoyd = new Person(4, "Roger", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
    Person feliciaBoyd = new Person(5, "Felicia", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6544", "jaboyd@email.com");
    Person jonathanMarrack = new Person(6, "Jonathan", "Marrack", "29 15th St", "Culver", "97451", "841-874-6513", "drk@email.com");
    Person tessaCarman = new Person(7, "Tessa", "Carman", "834 Binoc Ave", "Culver", "97451", "841-874-6512", "tenz@email.com");
    Person peterDuncan = new Person(8, "Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "jaboyd@email.com");

    MedicalRecord johnMedicalRecord = new MedicalRecord(1, 1, LocalDate.of(1984, 3, 6), medications, allergies);
    MedicalRecord jacobBoydMedicalRecord = new MedicalRecord(2, 2, LocalDate.of(1989, 3, 6), medications, allergies);
    MedicalRecord tenleyBoydMedicalRecord = new MedicalRecord(3, 3, LocalDate.now().minus(8, ChronoUnit.YEARS), medications, allergies);
    MedicalRecord rogerBoydMedicalRecord = new MedicalRecord(4, 4, LocalDate.now().minus(3, ChronoUnit.YEARS), medications, allergies);
    MedicalRecord feliciaBoydMedicalRecord = new MedicalRecord(5, 5, LocalDate.of(1986, 1, 8), medications, allergies);
    MedicalRecord jonathanMarrackMedicalRecord = new MedicalRecord(6, 6, LocalDate.of(1989, 1, 3), medications, allergies);
    MedicalRecord tessaCarmanMedicalRecord = new MedicalRecord(7, 7, LocalDate.of(2012, 2, 18), medications, allergies);
    MedicalRecord peterDuncanMedicalRecord = new MedicalRecord(8, 8, LocalDate.of(2000, 9, 6), medications, allergies);


    FireStation culverSt=new FireStation(1, "1509 Culver St", 3);
    FireStation th15St=new FireStation(2, "29 15th St", 2);
    FireStation binocAve=new FireStation(3, "834 Binoc Ave", 3);
    FireStation gershwinCir=new FireStation(4, "644 Gershwin Cir", 1);
    FireStation towningsDr=new FireStation(5, "748 Townings Dr", 3);
    FireStation steppesPl=new FireStation(6, "112 Steppes Pl", 3);

    @Tag("TestMethodgetFireStationCoverage")
    @Test
    void shouldReturnNumberOfChildrenAndAdultsForAStationNumber() throws Exception {
        // GIVEN
        String address = "1509 Culver St";
        int stationNumber=3;
        List<Person> lafamilleBoydCarman = Arrays.asList(johnBoyd, jacobBoyd, tenleyBoyd, rogerBoyd, feliciaBoyd,tessaCarman);
        List<FireStation> listeofStations = Arrays.asList(culverSt, binocAve);
        //when(personRepository.findAll()).thenReturn(lafamilleBoydCarman);
        when(fireStationRepository.findStationByStation(stationNumber)).thenReturn(listeofStations);
        when(personRepository.findPersonByAddress(address, Sort.by("address"))).thenReturn(lafamilleBoydCarman);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(1)).thenReturn(johnMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(2)).thenReturn(jacobBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(3)).thenReturn(tenleyBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(4)).thenReturn(rogerBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(5)).thenReturn(feliciaBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(7)).thenReturn(tessaCarmanMedicalRecord);

        // WHEN
        FireStationCoverage station = fireStationServiceImpl.getFireStationCoverage(stationNumber);

        //THEN
        assertThat(station.getFireStationPersons().size()).isEqualTo(6);
        assertThat(station.getAdultCount()).isEqualTo(3);
        assertThat(station.getChildCount()).isEqualTo(3);
    }

    @Tag("TestMethodgetPhoneNumbersByStation")
    @Test
    void shouldReturnPhoneNumbersForAStationNumber() {
        // GIVEN
        String address = "1509 Culver St";
        int stationNumber=3;
        List<Person> lafamilleBoydCarman = Arrays.asList(johnBoyd, jacobBoyd, tenleyBoyd, rogerBoyd, feliciaBoyd,tessaCarman);
        List<FireStation> listeofStations = Arrays.asList(culverSt, th15St);
        when(fireStationRepository.findStationByStation(stationNumber)).thenReturn(listeofStations);
        when(personRepository.findPersonByAddress(address, Sort.by("address"))).thenReturn(lafamilleBoydCarman);

        // WHEN
        List<PhoneCoverage> listPhones=fireStationServiceImpl.getPhoneNumbersByStation(stationNumber);

        //THEN
        assertThat(listPhones.size()).isEqualTo(3);
    }

    @Tag("TestMethodgetCoverageFireStationForSeveralFireStations")
    @Test
    void shouldReturnAllInformationsForSeveralStationNumber() {
        // GIVEN
        String address = "1509 Culver St";
        int stationNumber=3;
        String stationNumbers="2,3";
        List<Person> lafamilleBoydCarman = Arrays.asList(johnBoyd, jacobBoyd, tenleyBoyd, rogerBoyd, feliciaBoyd,tessaCarman);
        List<FireStation> listeofStations = Arrays.asList(culverSt, th15St);
        when(fireStationRepository.findStationByStation(stationNumber)).thenReturn(listeofStations);
        when(personRepository.findPersonByAddress(address, Sort.by("address"))).thenReturn(lafamilleBoydCarman);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(1)).thenReturn(johnMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(2)).thenReturn(jacobBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(3)).thenReturn(tenleyBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(4)).thenReturn(rogerBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(5)).thenReturn(feliciaBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByIdPerson(7)).thenReturn(tessaCarmanMedicalRecord);

        // WHEN
        List<FireStationsFlood> listOfInformations=fireStationServiceImpl.getCoverageFireStationForSeveralFireStations(stationNumbers);

        // THEN
        assertThat(listOfInformations.size()).isEqualTo(6);
        }

}
