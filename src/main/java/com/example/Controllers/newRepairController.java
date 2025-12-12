package com.example.Controllers;

import com.example.App;
import com.example.Database;
import com.example.Reparatie;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class newRepairController{
    @FXML
    TextArea textArea;
    
    @FXML
    public void add(){
        Reparatie reparatie = new Reparatie(App.currentDrone.getId(),textArea.getText());

        Database.newRepair(reparatie);
        Database.reloadDrones();
        Database.reloadReparatii();

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Succes");
        info.setHeaderText("Dronă adăugată la reparație !");
        info.setContentText("Drona "+App.currentDrone.getModel() + " a fost pusă în așteptare, reveniți mai târziu pentru a vizualiza prețul si a confirma reparația.");
        info.showAndWait();

        Stage stage = (Stage) textArea.getScene().getWindow();
        stage.close();


        System.out.println(textArea.getText());
    }
}
