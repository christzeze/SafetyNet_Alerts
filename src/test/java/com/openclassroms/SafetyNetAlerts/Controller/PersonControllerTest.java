package com.openclassroms.SafetyNetAlerts.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroms.SafetyNetAlerts.model.*;
import com.openclassroms.SafetyNetAlerts.service.PersonService;
import com.openclassroms.SafetyNetAlerts.service.PersonServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonServiceImpl personServiceImpl;

    @MockBean
    private Person personMock;


    List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
    List<String> allergies = Arrays.asList("nillacilan");
    List<String> stations = Arrays.asList("3");

    PersonInfos johnBoyd = new PersonInfos("John", "Boyd");
    PersonInfosFull johnBoydFull = new PersonInfosFull("John", "Boyd", 36);
    Child jacobBoyd = new Child("Jacob", "Boyd", 3);
    Email johnBoydEmail = new Email("test@gmail.com", "Culver");

    @Test
    public void PersonController_shouldReturnName_personInfo() throws Exception {

        //GIVEN :
        List<PersonInfos> personInfosMock = Arrays.asList(johnBoyd);

        when(personServiceImpl.getlistPersonsByFirstNameAndLastName(anyString(), anyString())).thenReturn(personInfosMock);


        //WHEN //THEN return the station added


        mockMvc.perform(get("/personInfo?firstName=John&lastName=Boyd")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName").value("John"))
                .andExpect(jsonPath("$..lastName").value("Boyd"));
    }

    @Test
    public void PersonController_shouldReturnNameAndAgeChild_childAlert() throws Exception {

        //GIVEN :

        String address = "1509 Culver St";
        List<Child> enfantBoyd = Arrays.asList(jacobBoyd);

        when(personServiceImpl.getlistOfChildren(address)).thenReturn(enfantBoyd);

        //WHEN //THEN

        mockMvc.perform(get("/childAlert?address=1509 Culver St")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName").value("Jacob"))
                .andExpect(jsonPath("$..lastName").value("Boyd"))
                .andExpect(jsonPath("$..age").value(3));
    }

    @Test
    public void PersonController_shouldReturnNameOfAPerson_fire() throws Exception {

        //GIVEN :

        String address = "1509 Culver St";
        List<PersonInfosFull> personInfosMock = Collections.singletonList(johnBoydFull);

        when(personServiceImpl.getAllInformationsForPersonnAtAnAddress(address)).thenReturn(personInfosMock);

        //WHEN //THEN

        mockMvc.perform(get("/fire?address=1509 Culver St")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName").value("John"))
                .andExpect(jsonPath("$..lastName").value("Boyd"))
                .andExpect(jsonPath("$..age").value(36));
    }

    @Test
    public void PersonController_shouldReturnEmailOfAPerson_communityEmail() throws Exception {

        //GIVEN :

        String city = "Culver";
        List<Email> personMailMock = Collections.singletonList(johnBoydEmail);

        when(personServiceImpl.getEmailPerCity(city)).thenReturn(personMailMock);

        //WHEN //THEN

        mockMvc.perform(get("/communityEmail?city=Culver")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..email").value("test@gmail.com"))
                .andExpect(jsonPath("$..city").value("Culver"));
    }

    @Test
    public void PersonController_shouldReturnStatusOkWhenSave() throws Exception {
        //GIVEN

        Person person = new Person("John", "Boyd");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(person);

        Person saved = new Person("John", "Boyd"); person.setId(1);
        when(personServiceImpl.save(person)).thenReturn(saved);

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void PersonController_shouldReturnStatusOkWhenUpdate() throws Exception {
        //GIVEN

        Person person = new Person("John", "Boyd");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(person);

        mockMvc.perform(put("/person?firstName=John&lastName=Boyd\"")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$..birthdate").value("03/06/1984"));
    }

    @Test
    public void PersonController_shouldReturnStatusOkWhenDelete() throws Exception {
        //GIVEN

        mockMvc.perform(delete("/person?firstName=John&lastName=Boyd\"")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$..birthdate").value("03/06/1984"));
    }

}
