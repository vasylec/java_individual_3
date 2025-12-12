package com.example.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.App;
import com.example.Database;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class confirmRentController implements Initializable {

    @FXML
    Label id,model,serial,sliderInfo,price;

    @FXML
    Slider slider;

    @FXML
    Button confirm;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        slider.valueProperty().addListener((obs, oldval, newVal) -> 
            slider.setValue(newVal.intValue())
        );
        sliderInfo.setText("Numar zile: " + (int)slider.getValue());
        price.setText("Pret : " + "0 MDL");

        slider.valueProperty().addListener((obs, oldval, newVal) -> {
            sliderInfo.setText("Numar zile: " + newVal.intValue());
            price.setText("Pret : " + (newVal.intValue() * 50) + " MDL");

        });

        
    
        id.setText("ID: " + App.currentDrone.getId());
        model.setText("Model: " + App.currentDrone.getModel());
        serial.setText("Numar de serie: " + App.currentDrone.getSerialNumber());
        
        

        confirm.setOnAction(e -> {
            
            try{
                Database.addRent(
                    App.currentDrone.getId(),
                    App.currentUser.getId(),
                    (int)slider.getValue() * 50,
                    (int)slider.getValue()
                );
    
                Database.reloadChirie();
                Database.reloadDrones();


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succes");
                alert.setHeaderText("Închiriere efectuată");
                alert.setContentText("Închirierea a fost adăugată cu succes!");
                alert.showAndWait();


                Stage stage = (Stage) confirm.getScene().getWindow();
                stage.close();
            }
            catch(Exception err){
                err.printStackTrace();
            }


        });




    }
    
}
