package com.example;

public class Drone {
    private int id;
    private String model;
    private String serialNumber;
    private String status;
    private int userId;
    private int price;

    public Drone(int id, String model, String serialNumber, String status, int userId, int price) {
        this.id = id;
        this.model = model;
        this.serialNumber = serialNumber;
        this.status = status;
        this.userId = userId;
        this.price = price;
    }
    public Drone(int id, String model, String serialNumber) {
        this.id = id;
        this.model = model;
        this.serialNumber = serialNumber;
        this.status = "disponibilă";
    }

    public Drone(String model, String serialNumber, int userId){
        this.model = model;
        this.serialNumber = serialNumber;
        this.userId = userId;
        status = "returnată";
    }

    public int getId() {
        return id;
    }
    public String getModel() {
        return model;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getUserId() {
        return userId;
    }
    public int getPrice() {
        return price;
    }
    
}
