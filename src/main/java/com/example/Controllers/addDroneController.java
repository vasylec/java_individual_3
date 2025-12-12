package com.example.Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.App;
import com.example.Database;
import com.example.Drone;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class addDroneController {
    @FXML
    TextField model, serial;
    
    @FXML
    public void add(){

        if(model.getText().isEmpty() || serial.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eroare");
            alert.setHeaderText("Nu puteți lăsa spații goale !");
            alert.setContentText("");
            alert.showAndWait();

            return;
        }


        for(Drone drone : App.droneList){
            if(drone.getSerialNumber().equals(serial.getText())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Eroare");
                alert.setHeaderText("Există deja o dronă cu acest număr de serie !");
                alert.setContentText("");
                alert.showAndWait();

                model.setText("");
                serial.setText("");
                return;
            }
        }



        Drone drone = new Drone(model.getText(), serial.getText(), App.currentUser.getId());

        String query = "INSERT INTO drones (model, serial_number, status) VALUES (?, ?, ?)";
        String query2 = "INSERT INTO drone_user (id_drone, id_user) VALUES (?, ?)";

        try (Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // INSERT în drones
            ps.setString(1, drone.getModel());
            ps.setString(2, drone.getSerialNumber());
            ps.setString(3, "personală");
            ps.executeUpdate();

            // Obține ID-ul generat
            int newDroneId = 0;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                newDroneId = rs.getInt(1);
            }

            if (newDroneId == 0) {
                throw new SQLException("ID-ul dronei nu a fost generat!");
            }

            // INSERARE în drone_user
            try (PreparedStatement ps2 = conn.prepareStatement(query2)) {
                ps2.setInt(1, newDroneId);
                ps2.setInt(2, drone.getUserId());
                ps2.executeUpdate();
            }

            Database.reloadDrones();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText("Adăugare efectuată");
            alert.setContentText("Adăugarea a fost efectuată cu succes!");
            alert.showAndWait();

            

            Stage stage = (Stage) model.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }




    }
}
