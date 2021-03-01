package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class FireStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String address;
    private int station;

    public FireStation(int id,String address, int station) {
        this.id=id;
        this.address = address;
        this.station = station;
    }

    public FireStation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    public FireStation() {}



}
