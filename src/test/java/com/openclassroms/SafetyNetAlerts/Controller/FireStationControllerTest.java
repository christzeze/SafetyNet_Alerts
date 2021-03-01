package com.openclassroms.SafetyNetAlerts.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroms.SafetyNetAlerts.model.*;


import com.openclassroms.SafetyNetAlerts.service.FireStationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc


public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationServiceImpl fireStationService;

    @MockBean
    private FireStation fireStationMock;

    List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
    List<String> allergies = Arrays.asList("nillacilan");
    List<String> stations = Arrays.asList("3");

    PhoneCoverage tel1=new PhoneCoverage("0000-00-001");
    PhoneCoverage tel2=new PhoneCoverage("0000-00-002");

    FireStationsFlood johnBoyd = new FireStationsFlood("John", "Boyd", "1509 Culver St",36,"841-874-6512", medications,allergies,stations);


    @Test
    public void FireStationController_shouldReturnAdultsAndChildrenCount() throws Exception {

        //GIVEN :
        FireStationCoverage fireStationCoverageMock = new FireStationCoverage();
        fireStationCoverageMock.setAdultCount(1);
        fireStationCoverageMock.setChildCount(2);

        when(fireStationService.getFireStationCoverage(anyInt())).thenReturn(fireStationCoverageMock);

        //WHEN //THEN return the station added


        mockMvc.perform(get("/fireStation?stationNumber="+1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..adultCount").value(1))
                .andExpect(jsonPath("$..childCount").value(2));

    }

    @Test
    public void FireStationController_getPhoneNumbersTest() throws Exception {

        //GIVEN :
        PhoneCoverage phoneCoverage = new PhoneCoverage();
        phoneCoverage.setPhone("000-000-001");
        List<PhoneCoverage> phoneCoverages = new ArrayList<>();
        phoneCoverages.add(phoneCoverage);

        List<PhoneCoverage> phoneCoverageMock=Arrays.asList(tel1);
        when(fireStationService.getPhoneNumbersByStation(anyInt())).thenReturn(phoneCoverages);

        //WHEN

        // THEN return the liste of telephon added
        mockMvc.perform(get("/phoneAlert?stationNumber="+1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..phone").value("000-000-001"));

    }

    @Test
    public void FireStationController_getCoverageFireStationForSeveralFireStationsTest() throws Exception {

        //GIVEN :

        List<FireStationsFlood> FireStationsFloodMock=Arrays.asList(johnBoyd);

        when(fireStationService.getCoverageFireStationForSeveralFireStations("3")).thenReturn(FireStationsFloodMock);

        //WHEN

        // THEN return the liste of telephon added
        mockMvc.perform(get("/flood/stations?stations="+3)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..lastName").value("Boyd"));

    }

    @Test
    public void FireStationController_shouldReturnStatusOkWhenSave() throws Exception {
        //GIVEN

        FireStation fireStation=new FireStation("1509 Culver St",3);


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(fireStation);

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$..birthdate").value("03/06/1984"));
    }

    @Test
    public void FireStationController_shouldReturnStatusOkWhenUpdate() throws Exception {
        //GIVEN

        FireStation fireStation=new FireStation("1509 Culver St",3);


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(fireStation);

        mockMvc.perform(put("/firestation?address=1509 Culver St")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$..birthdate").value("03/06/1984"));
    }

    @Test
    public void FireStationController_shouldReturnStatusOkWhenDelete() throws Exception {
        //GIVEN

        mockMvc.perform(delete("/firestation?address=1509 Culver St")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$..birthdate").value("03/06/1984"));
    }
}
