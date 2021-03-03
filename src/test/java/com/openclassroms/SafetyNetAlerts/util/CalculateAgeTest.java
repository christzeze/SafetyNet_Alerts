package com.openclassroms.SafetyNetAlerts.util;

import com.openclassroms.SafetyNetAlerts.service.CalculateAgeServiceImpl;
import org.junit.jupiter.api.Tag;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("com.openclassroms.SafetyNetAlerts")

public class CalculateAgeTest {

    @Autowired
    CalculateAgeServiceImpl calculateAgeServiceImpl;

    @Tag("TestonmethodcalculateAge")
    @Test
    public void shouldReturnAgeFromADate() {
        // GIVEN

        LocalDate date=LocalDate.of(1984, 3, 6);

        // WHEN
        int valeurAge = calculateAgeServiceImpl.calculateAge(date);

        //THEN
        assertThat(valeurAge).isEqualTo(36);
    }
}
