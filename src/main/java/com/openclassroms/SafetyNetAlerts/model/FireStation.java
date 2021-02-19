package com.openclassroms.SafetyNetAlerts.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class FireStation {
    @Id
    private int id;
    private String address;
    private String station;

    public FireStation(int id,String address, String station) {
        this.id=id;
        this.address = address;
        this.station = station;
    }

    public FireStation() {}


}
