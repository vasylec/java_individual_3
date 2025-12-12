package com.example.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import com.example.App;
import com.example.Database;
import com.example.Drone;
import com.example.Model.ChirieModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class userAppController implements Initializable{
    @FXML
    MenuItem newc, updatec, historyc, showMyDrones, addMyDrone, deleteMyDrone, newr, monitor, acceptRepair;
    
    @SuppressWarnings("rawtypes")
    @FXML
    TableView tableView, tableView1;

    @FXML
    TableColumn colId, colModel, colSerial, colStatus;
    @FXML
    TableColumn colId1, colModel1, colSerial1, colStart1, colEnd1, colPret1;

    @FXML
    ScrollPane scrollPane, scrollPane1;
    @FXML
    Button confirm;
    @FXML
    Label message;

    private static int confirmAction = 0;

    @SuppressWarnings("rawtypes")
    private ObservableList select(String status){
        ObservableList<Drone> list = FXCollections.observableArrayList();
        for (Drone drone : App.droneList) {
            if(drone.getStatus().equals(status)){
                list.add(drone);
            }
        }
        return list;
    }

    @SuppressWarnings("rawtypes")
    private ObservableList selectMyDrones(){
        ObservableList<Drone> list = FXCollections.observableArrayList();
        for (Drone drone : App.droneList) {
            if(drone.getUserId() == App.currentUser.getId()){
                list.add(drone);
            }
        }
        return list;
    }

    @SuppressWarnings("rawtypes")
    private ObservableList selectMyDronesForRepair(){
        ObservableList<Drone> list = FXCollections.observableArrayList();
        for (Drone drone : App.droneList) {
            if(drone.getUserId() == App.currentUser.getId() && drone.getStatus().equals("personală")){
                list.add(drone);
            }
        }
        return list;
    }

    @SuppressWarnings("rawtypes")
    private ObservableList selectMyDronesToAcceptRepair(){
        ObservableList<Drone> list = FXCollections.observableArrayList();
        for (Drone drone : App.droneList) {
            if(drone.getUserId() == App.currentUser.getId() && drone.getStatus().equals("spre confirmare")){
                list.add(drone);
            }
        }
        return list;
    }

    @SuppressWarnings("rawtypes")
    private ObservableList selectMyDronesRepair(){
        ObservableList<Drone> list = FXCollections.observableArrayList();
        for (Drone drone : App.droneList) {
            if(drone.getUserId() == App.currentUser.getId() && 
            (drone.getStatus().equals("în reparație") || drone.getStatus().equals("reparată") || drone.getStatus().equals("în așteptare"))
            ){
                list.add(drone);
            }
        }
        return list;
    }

    private void setVisible(String item){
        scrollPane.setVisible(false);
        scrollPane1.setVisible(false);
        confirm.setVisible(false);
        message.setVisible(false);
        scrollPane.setMouseTransparent(true);
        scrollPane1.setMouseTransparent(true);
        confirm.setMouseTransparent(true);

        if(item.equals("newc")){
            scrollPane.setVisible(true);
            message.setText("Apasati pe drona ce doriti sa o inchiriati apoi confirmati alegerea");
            message.setVisible(true);
            scrollPane.setMouseTransparent(false);
            confirm.setVisible(true);
            confirm.setMouseTransparent(false);
        }
        else if(item.equals("updatec")){
            scrollPane1.setVisible(true);
            scrollPane1.setMouseTransparent(false);
        }
        else if(item.equals("showMyDrones")){
            scrollPane.setVisible(true);
            scrollPane.setMouseTransparent(false);
            message.setText("Dronele personale");
            message.setVisible(true);
        }
        else if(item.equals("deleteMyDrones")){
            scrollPane.setVisible(true);
            scrollPane.setMouseTransparent(false);
            message.setText("Șterge o dronă personală");
            message.setVisible(true);
            confirm.setVisible(true);
            confirm.setMouseTransparent(false);
        }
        else if(item.equals("newr")){
            scrollPane.setVisible(true);
            scrollPane.setMouseTransparent(false);
            message.setText("Selectează drona pentru reparație !");
            message.setVisible(true);
            confirm.setVisible(true);
            confirm.setMouseTransparent(false);
        }
        else if(item.equals("monitor")){
            scrollPane.setVisible(true);
            scrollPane.setMouseTransparent(false);
            message.setText("Monitorizează reparații !");
            message.setVisible(true);
            // confirm.setVisible(true);
            // confirm.setMouseTransparent(false);
        }
        else if(item.equals("acceptRepair")){
            scrollPane.setVisible(true);
            scrollPane.setMouseTransparent(false);
            // message.setText("Monitorizează reparații !");
            // message.setVisible(true);
            confirm.setVisible(true);
            confirm.setMouseTransparent(false);
        }
    }

    @FXML
    public void logout(){
        try {
            Stage stage = (Stage) tableView.getScene().getWindow();
            App.scene = new Scene(App.loadFXML("login"));
            
            stage.setResizable(false);
            stage.setScene(App.scene);
            stage.show();

            App.currentUser = null;
            App.currentDrone = null;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void reloadChirieTable(){
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colSerial.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        setVisible("");
        
        newc.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.setItems(select("disponibilă"));
            setVisible("newc");
            confirmAction = 1;
        });

        updatec.setOnAction(e -> {
            colId1.setCellValueFactory(new PropertyValueFactory<>("droneId"));
            colModel1.setCellValueFactory(new PropertyValueFactory<>("model"));
            colSerial1.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
            colStart1.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            colEnd1.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            colPret1.setCellValueFactory(new PropertyValueFactory<>("price"));

            ObservableList<ChirieModel> list = FXCollections.observableArrayList();

            String sql = "SELECT drone_id, model, drones.serial_number, start_date, end_date, price " +
             "FROM chirie " +
             "INNER JOIN drones ON chirie.drone_id = drones.id " +
             "WHERE client = " + App.currentUser.getId() + " " +
             "AND end_date > CURRENT_DATE";

            try (Connection conn = Database.connect();
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery()) {

                while (rs.next()) {
                    list.add(
                        new ChirieModel(
                            rs.getInt("drone_id"),
                            rs.getString("model"),
                            rs.getString("serial_number"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getDouble("price")
                        )
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            tableView1.setItems(list);
            setVisible("updatec");
        });

        historyc.setOnAction(e -> {
            colId1.setCellValueFactory(new PropertyValueFactory<>("droneId"));
            colModel1.setCellValueFactory(new PropertyValueFactory<>("model"));
            colSerial1.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
            colStart1.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            colEnd1.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            colPret1.setCellValueFactory(new PropertyValueFactory<>("price"));

            ObservableList<ChirieModel> list = FXCollections.observableArrayList();

            String sql = "SELECT drone_id, model, drones.serial_number, start_date, end_date, price " +
             "FROM chirie " +
             "INNER JOIN drones ON chirie.drone_id = drones.id " +
             "WHERE client = " + App.currentUser.getId() + " " +
             "AND end_date < CURRENT_DATE";


            try (Connection conn = Database.connect();
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery()) {

                while (rs.next()) {
                    list.add(
                        new ChirieModel(
                            rs.getInt("drone_id"),
                            rs.getString("model"),
                            rs.getString("serial_number"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getDouble("price")
                        )
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            tableView1.setItems(list);
            setVisible("updatec");

        });

        confirm.setOnAction(e -> {
            if(confirmAction == 1){
                try {
                    App.currentDrone = (Drone) tableView.getSelectionModel().getSelectedItem();
    
                    Stage stage = new Stage();
                    Scene scene = new Scene(App.loadFXML("confirmRent"));
    
                    stage.setResizable(false);
                    stage.setScene(scene);
    
                    stage.setOnHidden(ev -> {
                        newc.fire();
                    });
                    stage.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else if (confirmAction == 2){
                App.currentDrone = (Drone) tableView.getSelectionModel().getSelectedItem();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmare");
                alert.setHeaderText("Sigur doriți să ștergeți următoarea dronă ?");
                alert.setContentText("Model: "+App.currentDrone.getModel()+";  Serial Number: "+App.currentDrone.getSerialNumber());

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        Iterator<Drone> iterator = App.droneList.iterator();

                        while (iterator.hasNext()) {
                            Drone d = iterator.next();
                            if (d.getId() == App.currentDrone.getId()) {
                                iterator.remove();
                            }
                        }
    
                        Database.deletePersonalDrone(App.currentDrone.getId());

                        Alert info = new Alert(Alert.AlertType.INFORMATION);
                        info.setTitle("Succes");
                        info.setHeaderText("Ștergere efectuată");
                        info.setContentText("Drona cu Serial Number: " + App.currentDrone.getSerialNumber()+ " a fost ștearsă !");
                        info.showAndWait();

                        tableView.getItems().clear();
                        tableView.setItems(selectMyDrones());
                        
                    } catch (Exception er) {
                        er.printStackTrace();
                    }
                    
                } else {
                    System.out.println("Ai anulat!");
                }
            }
            else if(confirmAction == 3){
                try {
                    App.currentDrone = (Drone) tableView.getSelectionModel().getSelectedItem();

                    Stage stage = new Stage();
                    Scene scene = new Scene(App.loadFXML("newRepair"));
    
                    stage.setResizable(false);
                    stage.setScene(scene);
    
                    stage.setOnHidden(ev -> {
                        
                    });
                    stage.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else if (confirmAction  == 4){
                
            }
            else if (confirmAction  == 5){
                
                if(tableView.getSelectionModel().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Alertă");
                    alert.setHeaderText("Selectează mai întâi o dronă");
                    alert.setContentText("");
                    alert.showAndWait();
                    return;
                }

                Drone drone = (Drone) tableView.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmare");
                alert.setHeaderText("Ești sigur?");
                alert.setContentText("Ești de acord cu condițiile reparării dronei : "+drone.getModel()+" ?");

                alert.getButtonTypes().clear();

                // Butoane personalizate
                ButtonType btnDa = new ButtonType("Da, sunt de acord", ButtonBar.ButtonData.OK_DONE);
                ButtonType btnCancel = new ButtonType("Renunță", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().addAll(btnDa, btnCancel);
                Optional<ButtonType> result = alert.showAndWait();


                if (result.isPresent()) {
                    if (result.get() == btnDa) {
                        try{
                            Database.updateDroneStatus(drone.getId(), "în reparație");
                            Database.reloadDrones();

                            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setTitle("Informație");
                            alert2.setHeaderText("Drona a fost pusă la reparație !");
                            alert2.setContentText("");
                            alert2.showAndWait();
                        }
                        catch(Exception err){
                            System.out.println("Eroare");
                        }  
                    } 
                    else if (result.get() == btnCancel) {
                        try{
                            Database.updateDroneStatus(drone.getId(), "personală");
                            Database.reloadDrones();

                            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setTitle("Informație");
                            alert2.setHeaderText("Drona a fost scoasă de la reparație !");
                            alert2.setContentText("");
                            alert2.showAndWait();
                        }
                        catch(Exception err){
                            System.out.println("Eroare");
                        }   
                    }
                } else {
                    System.out.println("Dialog închis fără selecție.");
                }

                acceptRepair.fire();

                
            }
        });

        showMyDrones.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.setItems(selectMyDrones());
            setVisible("showMyDrones");
        });

        addMyDrone.setOnAction(e -> {
            try {
                Stage stage = new Stage();
                Scene scene;
                scene = new Scene(App.loadFXML("addDrone"));
                
                stage.setResizable(false);
                stage.setScene(scene);

                stage.setOnHidden(ev -> {
                    showMyDrones.fire();
                });

                stage.show();

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });

        deleteMyDrone.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.setItems(selectMyDrones());
            setVisible("deleteMyDrones");
            confirmAction = 2;
        });

        newr.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.setItems(selectMyDronesForRepair());
            setVisible("newr");
            confirmAction = 3;
        });

        monitor.setOnAction(e-> {
            tableView.getItems().clear();
            tableView.setItems(selectMyDronesRepair());

            tableView.setRowFactory(tv -> new TableRow<Drone>() {
                @Override
                protected void updateItem(Drone item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setStyle("");
                        return;
                    }

                    // ==== AICI DECIZI CONDIȚIA ====

                    if ("spre confirmare".equals(item.getStatus())) {
                        setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");
                    } else {
                        setStyle("");
                    }
                }
            });

            setVisible("monitor");
            confirmAction = 4;
        });


        acceptRepair.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.setItems(selectMyDronesToAcceptRepair());
            setVisible("acceptRepair");
            confirmAction = 5;
        });


    }

}
