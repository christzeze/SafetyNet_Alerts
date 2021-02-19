package com.openclassroms.SafetyNetAlerts.repository;

import com.openclassroms.SafetyNetAlerts.model.Personn;
import lombok.Data;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonnRepository extends CrudRepository<Personn,String> {





}
