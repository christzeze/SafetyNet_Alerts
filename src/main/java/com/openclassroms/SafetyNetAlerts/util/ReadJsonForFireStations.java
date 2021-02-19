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
public class ReadJsonForFireStations {
    private JSONObject object;
    private JSONArray fireStations;


    /**
     * Liste des stations de la base de données
     */
    private List<FireStation> allFireStations;

    private int idCounter;

    private FireStationService fireStationService;



    private ReadJsonForFireStations(FireStationService fireStationService) {
        this.fireStationService=fireStationService;
    }

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("ReadJsonForFireStations");

    @PostConstruct
    public void initDataHandlerJsonFile() throws IOException, ParseException {
        this.loadFile();
        if (object == null) {
        } else {

            shareData(this.object);

            initFireStations();

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

        fireStations = (JSONArray) obj.get("firestations");

    }

    /**
     * Sauvegarde des données dans la base
     */
    public void initFireStations() {
        if (this.fireStations != null) {
            idCounter=1;
            Iterator<JSONObject> iterator = fireStations.iterator();
            while (iterator.hasNext()) {
                JSONObject fireStation = iterator.next();
                String address = (String) fireStation.get("address");
                String station = (String) fireStation.get("station");
                FireStation myFireStation=new FireStation(idCounter,address, station);
                this.allFireStations = new ArrayList<>();
                allFireStations.add(myFireStation);
                fireStationService.save(allFireStations);
                idCounter+=1;
            }
            logger.info("Succes of loading FireStations");
        } else {
            logger.error("Fail of Creation FireStations");
        }
    }
}
