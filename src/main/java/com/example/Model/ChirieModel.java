package com.example.Model;

import java.sql.Date;

public class ChirieModel {
  private int droneId;
  private String model;
  private String serialNumber;
  private Date startDate;
  private Date endDate;
  private double price;

  @SuppressWarnings("exports")
  public ChirieModel(int droneId, String model, String serialNumber, Date startDate, Date endDate, double price) {
    this.droneId = droneId;
    this.model = model;
    this.serialNumber = serialNumber;
    this.startDate = startDate;
    this.endDate = endDate;
    this.price = price;
  }

  @SuppressWarnings("exports")
  public ChirieModel(int droneId, String model, String serialNumber, Date startDate, double price) {
    this.droneId = droneId;
    this.model = model;
    this.serialNumber = serialNumber;
    this.startDate = startDate;
    this.endDate = null;
    this.price = price;
  }

  public int getDroneId() { return droneId; }
  public String getModel() { return model; }
  public String getSerialNumber() { return serialNumber; }
  public double getPrice() { return price; }
  @SuppressWarnings("exports")
  public Date getStartDate() { return startDate; }
  @SuppressWarnings("exports")
  public Date getEndDate() { return endDate; }
}
