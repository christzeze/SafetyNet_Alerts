package com.openclassroms.SafetyNetAlerts.repository;


import com.openclassroms.SafetyNetAlerts.model.Person;
import com.openclassroms.SafetyNetAlerts.model.PersonInfosFull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonFullRepository extends CrudRepository<PersonInfosFull,String> {
}
