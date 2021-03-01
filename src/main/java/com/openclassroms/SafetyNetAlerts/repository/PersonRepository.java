package com.openclassroms.SafetyNetAlerts.repository;


import com.openclassroms.SafetyNetAlerts.model.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, String> {

    List<Person> findPersonByFirstNameAndLastName(String firstName, String lastName);

    List<Person> findPersonByAddress(String address, Sort sort);

    List<Person> findPersonByCity(String city);
}
