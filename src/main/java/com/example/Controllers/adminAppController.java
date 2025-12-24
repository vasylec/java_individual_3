package com.example.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.example.App;
import com.example.Database;
import com.example.Drone;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class adminAppController implements Initializable{

	@FXML
	Label message,searchResult;

	@FXML
	TableView tableView;

	@FXML
	Pane search;
	@FXML
	ScrollPane scrollPane;
	@FXML
	MenuItem drone_disponibile, drone_inchiriate, drone_inreparatie, drone_reparate,
	drone_returnate, searchButton, repairConfirm,waitingForClientResponse, updateRepair;

	@FXML
	Button searchB;

	@FXML
	TextField droneIdTextField, droneSerialTextField;


	@FXML
	TableColumn<Drone, Integer> colId;
	@FXML
	TableColumn<Drone, String> colModel;
	@FXML
	TableColumn<Drone, String> colSerial;
	@FXML
	TableColumn<Drone, String> colStatus;
	@FXML
	TableColumn<Drone, Integer> colPrice;

	private int doubleClickEnabled = 0;


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
	private ObservableList select(String[] status){
		ObservableList<Drone> list = FXCollections.observableArrayList();
		for (Drone drone : App.droneList) {
			for(String s : status){
				if(drone.getStatus().equals(s)){
					list.add(drone);
				}
			}
		}
		return list;
	}

	private void setVisible(String type){
		scrollPane.setVisible(false);
		scrollPane.setMouseTransparent(true);
		search.setVisible(false);
		search.setMouseTransparent(true);
		doubleClickEnabled = 0;
		message.setVisible(false);

		if(type.equals("scrollPane")){
			scrollPane.setVisible(true);
			scrollPane.setMouseTransparent(false);
		}
		else if(type.equals("search")){
			search.setVisible(true);
			search.setMouseTransparent(false);
		}
		else if(type.equals("repairConfirm")){
			scrollPane.setVisible(true);
			scrollPane.setMouseTransparent(false);
			doubleClickEnabled = 1;
			message.setVisible(true);
		}
		else if(type.equals("updateRepair")){
			scrollPane.setVisible(true);
			scrollPane.setMouseTransparent(false);
			message.setVisible(true);
			doubleClickEnabled = 2;
		}
	}

	@FXML
	public void logout(){
		try {
			Stage stage = (Stage) tableView.getScene().getWindow();
			App.scene = new Scene(App.loadFXML("login"));
			App.scene.getStylesheets().clear();
			App.scene.getStylesheets().add(
				App.class.getResource("Style/loginStyle.css").toExternalForm()
			);

			stage.setResizable(false);
			stage.setScene(App.scene);
			stage.show();

			App.currentUser = null;
			App.currentDrone = null;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}	

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setVisible("");

		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
		colSerial.setCellValueFactory(new PropertyValueFactory<>("model"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

		tableView.setRowFactory(tv -> {
				TableRow<Drone> row = new TableRow<>();

				row.setOnMouseClicked(event -> {
					if(doubleClickEnabled == 0) return;
					else if(doubleClickEnabled == 1){
						if (event.getClickCount() == 2 && !row.isEmpty()) {
							Drone selectedDrone = row.getItem();

							int price = addPrice(selectedDrone);

							if(price == -1){
								return;
							}
							else{
								try{
									Database.updateDroneStatusAndRepairCost(selectedDrone.getId(),"spre confirmare",price);

									Alert info = new Alert(Alert.AlertType.INFORMATION);
									info.setTitle("Succes");
									info.setHeaderText("Prețul a fost introdus cu succes !");
									info.setContentText("");
									info.showAndWait();

									repairConfirm.fire();


								}
								catch(Exception er){
									er.printStackTrace();
								}
							}
						}
					}
					else if(doubleClickEnabled == 2){
						if (event.getClickCount() == 2 && !row.isEmpty()){

							Drone selectedDrone = row.getItem();
							List<String> valori = List.of(
								"în reparație",
								"reparată",
								"returnată"
							);

							ChoiceDialog<String> dialog = new ChoiceDialog<>(valori.get(0), valori);
							dialog.setTitle("Selectează noul status !");
							dialog.setHeaderText("Alege unul dintre statusurile disponibile\npentru drona: "+selectedDrone.getModel());
							dialog.setContentText("Status:");

							Optional<String> result = dialog.showAndWait();

							if(!result.isEmpty()){
								try {

									Database.updateDroneStatus(selectedDrone.getId(),result.get());
									Database.reloadDrones();

									Alert info = new Alert(Alert.AlertType.INFORMATION);
									info.setTitle("Succes");
									info.setHeaderText("Statusul a fost modificat cu succes !");
									info.setContentText("");
									info.showAndWait();

									updateRepair.fire();



								} catch (Exception e) {
									System.out.println("Eroare");
								}
							}

						}
					}


				});

				return row;
		});

		drone_disponibile.setOnAction(e -> {
			setVisible("scrollPane");

			tableView.setItems(select("disponibilă"));
		});

		drone_inchiriate.setOnAction(e -> {
			setVisible("scrollPane");

			tableView.setItems(select("închiriată"));
		});
		drone_inreparatie.setOnAction(e -> {
			setVisible("scrollPane");

			tableView.setItems(select("în reparație"));
		});
		drone_reparate.setOnAction(e -> {
			setVisible("scrollPane");

			tableView.setItems(select("reparată"));
		});
		drone_returnate.setOnAction(e -> {
			setVisible("scrollPane");

			tableView.setItems(select("returnată"));
		});
		searchButton.setOnAction(e -> {
			setVisible("search");

		});

		repairConfirm.setOnAction(e -> {
			setVisible("repairConfirm");
			tableView.setItems(select("în așteptare"));
		});


		waitingForClientResponse.setOnAction(e -> {
			setVisible("scrollPane");
			tableView.setItems(select("spre confirmare"));
		});

		updateRepair.setOnAction(e -> {
			setVisible("updateRepair");
			tableView.setItems(select(new String[]{"în reparație","reparată","returnată"}));
		});

		searchB.setOnAction(e -> {
			int id = 0;

         if(!droneIdTextField.getText().isEmpty()){
            id = Integer.parseInt(droneIdTextField.getText());
         }
			String serial = droneSerialTextField.getText();
         
			for(Drone drone : App.droneList){
            System.out.println("DRONEID"+drone.getId());
            if(drone.getId() == id){
					String prop = drone.getUserId() + "";
					if(drone.getUserId() == 0) prop = "Companie";
					searchResult.setText("ID: "+drone.getId()+
					"\nModel: "+drone.getModel()+"\n"+
					"Seria: "+drone.getSerialNumber()+"\n"+
					"Status: "+drone.getStatus()+"\n"+
					"Proprietar: "+prop
					);

               break;
				}
            else if(drone.getSerialNumber().equals(serial)){
               String prop = drone.getUserId() + "";
               if(drone.getUserId() == 0) prop = "Companie";
               searchResult.setText("ID: "+drone.getId()+
               "\nModel: "+drone.getModel()+"\n"+
               "Seria: "+drone.getSerialNumber()+"\n"+
               "Status: "+drone.getStatus()+"\n"+
               "Proprietar: "+prop
               );

               break;
            }
            else{
               searchResult.setText("Nu s-a găsit nimic !");
            }
			}
		});
	
   
      droneIdTextField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
         droneSerialTextField.setText("");
         if(event.getCode() == KeyCode.ENTER){
            searchB.fire();
         }
      });
      droneSerialTextField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
         droneIdTextField.setText("");
         if(event.getCode() == KeyCode.ENTER){
            searchB.fire();
         }
      });
      
   
   }




	private String wrapWords(String text, int lineLength) {
    StringBuilder sb = new StringBuilder();
    int lineCount = 0;

    for (String word : text.split(" ")) {

        // Dacă cuvântul nu încape pe linia curentă → linie nouă
        if (lineCount + word.length() > lineLength) {
            sb.append("\n");
            lineCount = 0;
        }

        // Adaugă cuvântul
        sb.append(word).append(" ");
        lineCount += word.length() + 1;
    }

    return sb.toString().trim();
}


	private int addPrice(Drone drone) {
		String descriere = wrapWords(drone.getDescriere(), 75);	

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Introduceți prețul");
		dialog.setHeaderText("Descriere introdusă de client:\n\n" + descriere);
		dialog.setContentText("Introduceți o singură valoare întreagă:");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {

		try {
			return Integer.parseInt(result.get());
		} catch (NumberFormatException e) {
			Alert info = new Alert(Alert.AlertType.ERROR);
			info.setTitle("EROARE");
			info.setHeaderText("Prețul introdus este invalid !");
			info.setContentText("Adaugă un preț doar din valori întregi !");
			info.showAndWait();
	
			return addPrice(drone); 
		}

		} else {
			return -1;
		}		

	}


}
