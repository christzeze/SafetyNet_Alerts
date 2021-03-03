package com.openclassroms.SafetyNetAlerts.service;

import com.openclassroms.SafetyNetAlerts.dto.PersonInfos;
import com.openclassroms.SafetyNetAlerts.model.*;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc


public class PersonServiceTest {
    private List<Person> abstractPeople;
    private List<FireStation> stations;
    private List<MedicalRecord> medicalRecords;

    @Autowired
    PersonServiceImpl personServiceImpl;

    @Autowired
    CalculateAgeServiceImpl calculateAgeServiceImpl;

    @MockBean
    private MedicalRecordServiceImpl medicalRecordServiceImpl2;

    @MockBean
    private FireStationServiceImpl fireStationServiceImpl;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
    List<String> allergies = Arrays.asList("nillacilan");

    Person johnBoyd = new Person(1, "John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
    Person jacobBoyd = new Person(2, "Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
    Person tenleyBoyd = new Person(3, "Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
    Person rogerBoyd = new Person(4, "Roger", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
    Person feliciaBoyd = new Person(5, "Felicia", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6544", "jaboyd@email.com");

    MedicalRecord johnMedicalRecord = new MedicalRecord(1, johnBoyd, LocalDate.of(1984, 3, 6), medications, allergies);
    MedicalRecord jacobBoydMedicalRecord = new MedicalRecord(2, jacobBoyd, LocalDate.of(1989, 3, 6), medications, allergies);
    MedicalRecord tenleyBoydMedicalRecord = new MedicalRecord(3, tenleyBoyd, LocalDate.now().minus(8, ChronoUnit.YEARS), medications, allergies);
    MedicalRecord rogerBoydMedicalRecord = new MedicalRecord(4, rogerBoyd, LocalDate.now().minus(3, ChronoUnit.YEARS), medications, allergies);
    MedicalRecord feliciaBoydMedicalRecord = new MedicalRecord(5, feliciaBoyd, LocalDate.of(1986, 1, 8), medications, allergies);

    FireStation culverSt=new FireStation(1, "1509 Culver St", 3);
    FireStation th15St=new FireStation(2, "29 15th St", 2);
    FireStation binocAve=new FireStation(3, "834 Binoc Ave", 3);
    FireStation gershwinCir=new FireStation(4, "644 Gershwin Cir", 1);
    FireStation towningsDr=new FireStation(5, "748 Townings Dr", 3);
    FireStation steppesPl=new FireStation(6, "112 Steppes Pl", 3);


    @Tag("TestonmethodgetlistOfChildren")
    @Test
    void shouldReturListOfChildrenAndAdultsForAListOf5Persons() throws Exception {
        // GIVEN
        String address = "1509 Culver St";
        List<Person> lafamilleBoyd = Arrays.asList(johnBoyd, jacobBoyd, tenleyBoyd, rogerBoyd, feliciaBoyd);
        when(personRepository.findPersonByAddress(address, Sort.by("address"))).thenReturn(lafamilleBoyd);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(1)).thenReturn(johnMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(2)).thenReturn(jacobBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(3)).thenReturn(tenleyBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(4)).thenReturn(rogerBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(5)).thenReturn(feliciaBoydMedicalRecord);

        // WHEN
        List<Child> childList = personServiceImpl.getlistOfChildren(address);

        //THEN
        assertThat(childList.size()).isEqualTo(2);
        assertThat(childList).containsExactlyInAnyOrder(new Child(tenleyBoyd.getFirstName(), tenleyBoyd.getLastName()), new Child(rogerBoyd.getFirstName(), rogerBoyd.getLastName()));
    }



    @Tag("TestonmethodgetEmailPerCity")
    @Test
    void shouldReturnAllEmailsForAListOf5Persons() throws Exception {
        //GIVEN
        String city="Culver";
        List<Person> lafamilleBoyd = Arrays.asList(johnBoyd, jacobBoyd, tenleyBoyd, rogerBoyd, feliciaBoyd);
        when(personRepository.findPersonByCity(city)).thenReturn(lafamilleBoyd);

        // WHEN
        List<Email> emailList = personServiceImpl.getEmailPerCity(city);

        //THEN
        assertThat(emailList.size()).isEqualTo(5);

    }

    @Tag("TestonmethodgetAllInformationsForPersonnAtAnAddress")
    @Test
    void shouldReturnAllInformationsForAListOf5Persons() {
        //GIVEN
        String address = "1509 Culver St";
        List<Person> lafamilleBoyd = Arrays.asList(johnBoyd, jacobBoyd, tenleyBoyd, rogerBoyd, feliciaBoyd);
        when(personRepository.findPersonByAddress(address, Sort.by("address"))).thenReturn(lafamilleBoyd);
        when(personRepository.findPersonByAddress(address, Sort.by("address"))).thenReturn(lafamilleBoyd);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(1)).thenReturn(johnMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(2)).thenReturn(jacobBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(3)).thenReturn(tenleyBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(4)).thenReturn(rogerBoydMedicalRecord);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(5)).thenReturn(feliciaBoydMedicalRecord);

        //WHEN
        List<Person> AllInformationsList=personServiceImpl.getAllInformationsForPersonnAtAnAddress(address);

        //THEN
        assertThat(AllInformationsList.size()).isEqualTo(5);


    }

    @Tag("TestMethodgetlistPersonsByFirstNameAndLastName")
    @Test
    void shouldReturnInformationsForAPerson() {
        //GIVEN
        String firstName="John";
        String lastName="Boyd";
        List<Person> lafamilleBoyd = Arrays.asList(johnBoyd);
        when(personRepository.findPersonByFirstNameAndLastName(firstName,lastName)).thenReturn(lafamilleBoyd);
        when(medicalRecordRepository.findFirstMedicalRecordByPersonId(1)).thenReturn(johnMedicalRecord);

        //WHEN
        List<PersonInfos> allInformationsList=personServiceImpl.getlistPersonsByFirstNameAndLastName(firstName,lastName);

        //THEN
        assertThat(allInformationsList.size()).isEqualTo(1);

    }



}
