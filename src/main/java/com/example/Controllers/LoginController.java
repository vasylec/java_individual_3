package com.example.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.example.App;
import com.example.User;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController implements Initializable{

    @FXML 
    Button button;
    @FXML
    Label info;
    @FXML
    PasswordField pass;
    @FXML
    TextField login;

    private void infoResetOpacity(){
        info.setOpacity(0);
    }

    private int checkLogin(String log, String pass) throws Exception{
        for (User user : App.userList) {
            if(user.getLogin().equals(log) && App.verify(user.getPassword(), pass.toCharArray())/*user.getPassword().equals(pass)*/ && user.getIsAdmin()){
                App.currentUser = user;
                return 2;
            }
            else if(user.getLogin().equals(log) && App.verify(user.getPassword(), pass.toCharArray())/*user.getPassword().equals(pass)*/ && !user.getIsAdmin()){
                App.currentUser = user;
                return 1;
            }
        }
        return 0;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        pass.setOnKeyPressed(e -> infoResetOpacity());
        login.setOnKeyPressed(e -> infoResetOpacity());
        
        
        pass.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.ENTER){
                button.fire();
            }
        });
        login.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.ENTER){
                button.fire();
            }
        });

        button.setOnAction(e -> {
            try {
                if(checkLogin(login.getText(),pass.getText()) == 2){
                    info.setOpacity(0);
                    try {
                        App.scene.setRoot(App.loadFXML("adminApp"));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else if(checkLogin(login.getText(),pass.getText()) == 1){
                    try {
                        App.scene.setRoot(App.loadFXML("userApp"));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }   
                }
                // else if(login.getText().equals("")){
                //     try {
                //         App.scene.setRoot(App.loadFXML("userApp"));
                //     } catch (IOException e1) {
                //         e1.printStackTrace();
                //     }   
                // }
                else{
                    info.setOpacity(1);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }
    
}
