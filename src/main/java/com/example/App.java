package com.example;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 * JavaFX App
 */
public class App extends Application {

    @SuppressWarnings("exports")
    public static Scene scene;
    private static Stage stage;
    public static ObservableList<Drone> droneList = FXCollections.observableArrayList();
    public static ObservableList<Chirie> chirieList = FXCollections.observableArrayList();
    public static ObservableList<Reparatie> reparatieList = FXCollections.observableArrayList();
    public static ObservableList<User> userList = FXCollections.observableArrayList();
    public static User currentUser = null;
    public static Drone currentDrone = null;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALGO = "PBKDF2WithHmacSHA256";

    public static String hashPassword(char[] password) throws Exception {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        int iterations = 200_000; // crește în funcție de performanță (ex: 100k-500k)
        int keyLength = 256;

        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGO);
        byte[] hash = skf.generateSecret(spec).getEncoded();

        // Format de stocare: iterations:saltBase64:hashBase64
        String result = iterations + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);

        // curățare
        spec.clearPassword();
        Arrays.fill(password, '\0');
        return result;
    }

    public static boolean verify(String stored, char[] candidate) throws Exception {
        String[] parts = stored.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = Base64.getDecoder().decode(parts[1]);
        byte[] hash = Base64.getDecoder().decode(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(candidate, salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGO);
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        // comparatie constant-time
        boolean matches = slowEquals(hash, testHash);
        spec.clearPassword();
        Arrays.fill(candidate, '\0');
        return matches;
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }



    @SuppressWarnings("static-access")
    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws IOException {        
        this.stage = stage;
        scene = new Scene(loadFXML("login"));
        
        try{
            Database.updateDronesAvailability();
            Database.reloadDrones();
            Database.reloadChirie();
            Database.reloadReparatii();
            Database.reloadUsers();
        }
        catch(Exception err){
            err.printStackTrace();
            scene = new Scene(loadFXML("connectError"));
        }

        try {
            for (User user : App.userList) {
                char[] pw = user.getPassword().toCharArray();
                String stored = hashPassword(pw);
                user.setPassword(stored);
                // System.out.println("Stored: " + stored);
                // boolean ok = verify(stored, user.getPassword().toCharArray());
                // System.out.println("OK? " + ok);
            }
        } catch (Exception e) {
            System.out.println("ERROR LA CRIPTARE");
            Platform.exit();
            return;
        }

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();


        
        
        //vasile
        //cozma1401

        //cozma
        //vasile1401


        // Afisare in consola din tabelul User
        for(User user : userList){
            System.out.println("---------------------Utilizatori-----------------------");
            System.out.println("ID: "+user.getId());
            System.out.println("LOGIN: "+user.getLogin());
            System.out.println("PAROLA: "+user.getPassword());
            System.out.println("--------------------------------------------");
        }


        //Afisare in consola din tabelul Reparatie
        // for(Reparatie reparatie : reparatieList){
        //     System.out.println(reparatie.getId());
        //     System.out.println(reparatie.getDrone_id());
        //     System.out.println(reparatie.getData_reparatie());
        //     System.out.println(reparatie.getDescriere());
        //     System.out.println(reparatie.getCost());
        //     System.out.println();
        // }

        //Afisare in consola din tabelul Chirie
        // for(Chirie chirie : chirieList){
        //     System.out.println(chirie.getId());
        //     System.out.println(chirie.getDrone_id());
        //     System.out.println(chirie.getClient());
        //     System.out.println(chirie.getPrice());
        //     System.out.println(chirie.getStart_date());
        //     System.out.println(chirie.getEnd_date());
        // }

        // Afisare in consola din tabelul Drones
        // for (Drone drone : droneList) {
        //     System.out.println("--------------------------------------");
        //     System.out.println(drone.getId());
        //     System.out.println(drone.getModel());
        //     System.out.println(drone.getSerialNumber());
        //     System.out.println(drone.getStatus());
        //     System.out.println(drone.getUserId());
        //     System.out.println(drone.getPrice());
        //     System.out.println(drone.getDescriere());
        //     System.out.println("--------------------------------------");
        // }




    }

    public static void exitApp(){
        stage.close();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    @SuppressWarnings("exports")
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}