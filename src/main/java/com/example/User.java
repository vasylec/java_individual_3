package com.example;

// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;

public class User {
    private int id;
    private String login;
    private String password;
    private boolean isAdmin;
    // private ObservableList<String> droneInchiriate = FXCollections.observableArrayList();
    // private ObservableList<String> droneReparatie = FXCollections.observableArrayList();

    User(int id, String login, String password, boolean isAdmin){
        this.id = id;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public int getId() {
        return id;
    }
    public boolean getIsAdmin(){
        return isAdmin;
    }
    public void setPassword(String password) {
       this.password = password;
    }
}
