package com.openclassroms.SafetyNetAlerts.util;

import com.openclassroms.SafetyNetAlerts.model.FireStation;
import com.openclassroms.SafetyNetAlerts.model.Personn;
import com.openclassroms.SafetyNetAlerts.service.FireStationService;
import com.openclassroms.SafetyNetAlerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ReadJsonForPersons {
    private JSONObject object;
    private JSONArray persons;


    /**
     * Liste des personnes de la base de données
     */
    private List<Personn> allPersons;

    private int idCounter;

    private PersonService personService;



    private ReadJsonForPersons(PersonService personService) {
        this.personService=personService;
    }

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("ReadJsonForPersons");

    @PostConstruct
    public void initDataHandlerJsonFile() throws IOException, ParseException {
        this.loadFile();
        if (object == null) {

        } else {

            shareData(this.object);

            initPerson();

        }
    }

    /**
     * Chargement des données JSON
     */
    public void loadFile()  {
        try {
            JSONParser jsonParser = new JSONParser();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("./data.json");
            this.object = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream));
            logger.info("Data loaded");
        } catch (Exception e) {
            logger.error(new StringBuffer("Data can't be loaded in Db : ").append(e));
        }

    }

    public void shareData(JSONObject obj) {

        persons = (JSONArray) obj.get("persons");


    }

    /**
     * Sauvegarde des données dans la base
     */
    public void initPerson() {
        if (persons != null) {
            idCounter=1;
            Iterator<JSONObject> iterator = persons.iterator();
            while (iterator.hasNext()) {
                JSONObject person = iterator.next();
                String firstName = (String) person.get("firstName");
                String lastName = (String) person.get("lastName");
                String city=(String) person.get("city");
                String zip=(String) person.get("zip");
                String phone = (String) person.get("phone");
                String mail = (String) person.get("email");
                String address = (String) person.get("address");
                Personn myPersonn=new Personn(idCounter,firstName, lastName, address,city,zip,phone,mail);
                this.allPersons = new ArrayList<>();
                allPersons.add(myPersonn);
                personService.save(allPersons);
                idCounter+=1;
            }

            logger.info("Succes of loading Persons");
        } else {
            logger.error("Fail of Creation Persons");
        }

    }
}
