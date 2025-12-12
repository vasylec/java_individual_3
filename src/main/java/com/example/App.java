package com.example;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import javafx.application.Application;
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

    
    // private static SecretKey generateKey(String password, byte[] salt) throws Exception {
    //     SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    //     PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
    //     return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    // }

    // public static String encrypt(String plainText, String password) throws Exception {
    //     byte[] salt = new byte[16];
    //     byte[] iv = new byte[16];

    //     SecureRandom random = new SecureRandom();
    //     random.nextBytes(salt);
    //     random.nextBytes(iv);

    //     SecretKey key = generateKey(password, salt);
    //     Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    //     cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    //     byte[] encrypted = cipher.doFinal(plainText.getBytes());

    //     // Returnăm salt + iv + text criptat în Base64
    //     byte[] combined = new byte[salt.length + iv.length + encrypted.length];
    //     System.arraycopy(salt, 0, combined, 0, salt.length);
    //     System.arraycopy(iv, 0, combined, salt.length, iv.length);
    //     System.arraycopy(encrypted, 0, combined, salt.length + iv.length, encrypted.length);

    //     return Base64.getEncoder().encodeToString(combined);
    // }

    // public static String decrypt(String base64, String password) throws Exception {
    //     byte[] combined = Base64.getDecoder().decode(base64);

    //     byte[] salt = new byte[16];
    //     byte[] iv = new byte[16];
    //     byte[] encrypted = new byte[combined.length - 32];

    //     System.arraycopy(combined, 0, salt, 0, 16);
    //     System.arraycopy(combined, 16, iv, 0, 16);
    //     System.arraycopy(combined, 32, encrypted, 0, encrypted.length);

    //     SecretKey key = generateKey(password, salt);

    //     Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    //     cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    //     byte[] decrypted = cipher.doFinal(encrypted);

    //     return new String(decrypted);
    // }

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
            scene = new Scene(loadFXML("connectError"));
        }
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();


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
            stage.close();
        }
        
        //vasile
        //cozma1401

        //cozma
        //vasile1401


        // Afisare in consola din tabelul User
        for(User user : userList){
            System.out.println(user.getId());
            System.out.println(user.getLogin());
            System.out.println(user.getPassword());
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
        //     System.out.println(drone.getId());
        //     System.out.println(drone.getModel());
        //     System.out.println(drone.getSerialNumber());
        //     System.out.println(drone.getStatus());
        //     System.out.println(drone.getUserId());
        //     System.out.println(drone.getPrice());
        //     System.out.println();
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