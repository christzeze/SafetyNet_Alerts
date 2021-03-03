package com.openclassroms.SafetyNetAlerts.repository;

import com.openclassroms.SafetyNetAlerts.model.Person;
import com.openclassroms.SafetyNetAlerts.service.PersonServiceImpl;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("com.openclassroms.SafetyNetAlerts")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class PersonRepositoryTest {
    @Autowired
    private PersonServiceImpl personService;

    @Test
    public void shouldReturnFirstNameAndLastNameWhenSave() {
        //GIVEN
        String firstName="John";
        String lastName="Boyd";

        Person abstractPersonDetails =new Person(firstName,lastName);

        //WHEN
        Person abstractPerson =personService.save(abstractPersonDetails);

        //THEN
        assertThat(abstractPerson.getFirstName()).isEqualTo("John");
        assertThat(abstractPerson.getLastName()).isEqualTo("Boyd");

    }

    @Test
    public void shouldReturnFirstNameAndLastNameWhenUpdate() {
        //GIVEN
        String firstName="John";
        String lastName="Boyd";

        Person abstractPersonDetails =new Person(firstName,lastName);

        //WHEN
        Person person=personService.updatePerson(firstName,lastName, abstractPersonDetails);


        //THEN
        assertThat(person.getFirstName()).isEqualTo("John");
        assertThat(person.getLastName()).isEqualTo("Boyd");

    }



}
