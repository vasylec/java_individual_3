package com.example;

import java.sql.Date;

public class Reparatie {
    private int id;
    private int drone_id;
    private Date data_reparatie;
    private String descriere;
    private int cost;

    Reparatie(int id, int drone_id, Date data_reparatie, String descriere, int cost){
        this.id = id;
        this.drone_id = drone_id;
        this.data_reparatie = data_reparatie;
        this.descriere = descriere;
        this.cost = cost;
    }
    public Reparatie(int drone_id,String descriere){
        this.drone_id = drone_id;
        this.descriere = descriere;
    }

    public int getCost() {
        return cost;
    }
    @SuppressWarnings("exports")
    public Date getData_reparatie() {
        return data_reparatie;
    }
    public String getDescriere() {
        return descriere;
    }
    public int getDrone_id() {
        return drone_id;
    }
    public int getId() {
        return id;
    }
}
