package com.openclassroms.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroms.SafetyNetAlerts.model.*;
import com.openclassroms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassroms.SafetyNetAlerts.service.MedicalRecordServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class MedicalRecordControllerTest  {



    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @MockBean
    private MedicalRecordServiceImpl medicalRecordServiceImpl;

    @MockBean
    private MedicalRecord medicalRecordMock;




    List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
    List<String> allergies = Arrays.asList("nillacilan");

    @Test
    public void MedicalRecordController_shouldReturnStatusOkWhenSave() throws Exception {
        //GIVEN

        MedicalRecord medicalRecord=new MedicalRecord(LocalDate.of(1984, 3, 6),medications, allergies);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(medicalRecord);

        mockMvc.perform(post("/medicalRecord?firstName=Jacques&lastName=Martineaux")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                //.content(new ObjectMapper().writeValueAsString(medicalRecord)))
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void MedicalRecordController_shouldReturnStatusOkWhenUpdate() throws Exception {
        //GIVEN

        MedicalRecord medicalRecord=new MedicalRecord(LocalDate.of(1984, 3, 6),medications, allergies);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(medicalRecord);


        mockMvc.perform(put("/medicalRecord?firstName=Jacques&lastName=Martineaux")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());

    }





}
