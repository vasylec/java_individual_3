package com.example;

import java.sql.Date;

public class Chirie {
    private int id;
    private int drone_id;
    private Date start_date;
    private Date end_date;
    private int client;
    private int price;

    Chirie(int id, int drone_id, Date start_date, Date end_date, int client, int price){
        this.id = id;
        this.drone_id = drone_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.client = client;
        this.price = price;
    }
    Chirie(int id, int drone_id, Date start_date, int client, int price){
        this.id = id;
        this.drone_id = drone_id;
        this.start_date = start_date;
        this.end_date = null;
        this.client = client;
        this.price = price;
    }
    Chirie(int drone_id, Date start_date, Date end_date , int client, int price){
        this.drone_id = drone_id;
        this.start_date = start_date;
        this.end_date = null;
        this.client = client;
        this.price = price;
    }
    public int getClient() {
        return client;
    }
    public int getDrone_id() {
        return drone_id;
    }
    @SuppressWarnings("exports")
    public Date getEnd_date() {
        return end_date;
    }
    public int getId() {
        return id;
    }
    public int getPrice() {
        return price;
    }
    @SuppressWarnings("exports")
    public Date getStart_date() {
        return start_date;
    }
    
}
