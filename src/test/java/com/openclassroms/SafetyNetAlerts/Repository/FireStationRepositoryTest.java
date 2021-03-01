package com.openclassroms.SafetyNetAlerts.Repository;

import com.openclassroms.SafetyNetAlerts.model.FireStation;
import com.openclassroms.SafetyNetAlerts.service.FireStationServiceImpl;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("com.openclassroms.SafetyNetAlerts")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class FireStationRepositoryTest {
    @Autowired
    private FireStationServiceImpl fireStationService;

    @Test
    public void shouldReturnFirstNameAndLastNameWhenSave() {
        //GIVEN
        String address="1509 Culver St";
        int station=3;

        FireStation fireStationDetails=new FireStation(address,station);

        //WHEN
        FireStation fireStation=fireStationService.save(fireStationDetails);

        //THEN
        assertThat(fireStation.getAddress()).isEqualTo("1509 Culver St");
        assertThat(fireStation.getStation()).isEqualTo(3);

    }

    @Test
    public void shouldReturn200WhenUpdate() {
        //GIVEN
        String address="1509 Culver St";
        int station=3;

        FireStation fireStationDetails=new FireStation(address,station);

        //WHEN
        ResponseEntity responseEntity=fireStationService.updateFireStation(address,fireStationDetails);
        int obj = responseEntity.getStatusCodeValue();

        //THEN
        assertThat(obj).isEqualTo(200);

    }


}
