package com.example;

import java.sql.*;
import java.time.LocalDate;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/drone_repair";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @SuppressWarnings("exports")
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateDronesAvailability() {
        String query = 
            "UPDATE drones d " +
            "JOIN chirie c ON d.id = c.drone_id " +
            "SET d.status = 'disponibila' " +
            "WHERE c.end_date < CURDATE()";

        try (Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query)) {

            int rows = ps.executeUpdate();
            System.out.println("Drone actualizate: " + rows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void reloadUsers(){
        App.userList.clear();
        String query = "SELECT * FROM user";
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()
        ){
            while (rs.next()) {
                boolean isAdmin = rs.getInt("isAdmin") == 1 ? true : false;
                App.userList.add(new User(
                    rs.getInt("id"), 
                    rs.getString("login"), 
                    rs.getString("password"),
                    isAdmin
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void reloadReparatii(){
        App.chirieList.clear();
        String query = "SELECT * FROM reparatii";
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()
        ){
            while (rs.next()) {
                App.reparatieList.add(new Reparatie(
                    rs.getInt("id"),
                    rs.getInt("drone_id"),
                    rs.getDate("data_reparatie"),
                    rs.getString("descriere"),
                    rs.getInt("cost")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void reloadChirie(){
        App.chirieList.clear();
        String query = "SELECT * FROM chirie";
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()
        ){
            while (rs.next()) {
                App.chirieList.add(new Chirie(
                    rs.getInt("id"),
                    rs.getInt("drone_id"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    rs.getInt("client"),
                    rs.getInt("price")
                ));
            }
            System.out.println("RELOADED CHIRIE");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void reloadDrones(){
        App.droneList.clear();
        String query = "SELECT drones.id, drones.model, drones.serial_number, drones.status, drone_user.id_user, reparatii.cost, reparatii.descriere FROM drones \r\n" + //
                        "LEFT JOIN drone_user ON drone_user.id_drone = drones.id\r\n" + //
                        "LEFT JOIN reparatii ON reparatii.drone_id = drones.id";
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
        ){
            while (rs.next()) {
                App.droneList.add(new Drone(
                    rs.getInt("id"), 
                    rs.getString("model"), 
                    rs.getString("serial_number"), 
                    rs.getString("status"),
                    rs.getInt("id_user"),
                    rs.getInt("cost"),
                    rs.getString("descriere")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void addRent(int idDrone, int idClient, int price, int days) {
        String query = "INSERT INTO chirie (drone_id, start_date, end_date, client, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query)) {
                
            updateDroneStatus(idDrone, "închiriată");

            LocalDate start = LocalDate.now();
            LocalDate end = start.plusDays(days);

            ps.setInt(1, idDrone);
            ps.setDate(2, java.sql.Date.valueOf(start));
            ps.setDate(3, java.sql.Date.valueOf(end));
            ps.setInt(4, idClient);
            ps.setInt(5, price);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePersonalDrone(int id_drone){
        String query = "DELETE FROM drones WHERE id = ?";
        String query2 = "DELETE FROM drone_user WHERE id_drone = ?";
        // String query3 = "DELETE FROM reparatii WHERE id_drone = ?";
        try(
            Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            PreparedStatement ps2 = conn.prepareStatement(query2);
            // PreparedStatement ps3 = conn.prepareStatement(query3);
        ){
            // ps3.setInt(1, id_drone);
            ps2.setInt(1, id_drone);
            ps.setInt(1, id_drone);
            // ps3.executeUpdate();
            ps2.executeUpdate();
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateChirie(Chirie chirie){
        String query = "UPDATE chirie SET drone_id = ?, start_date = ?, end_date = ?, client = ?, price = ? WHERE id = ?";
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query)
        ){
            ps.setInt(1, chirie.getDrone_id());
            ps.setDate(2, chirie.getStart_date());
            ps.setDate(3, chirie.getEnd_date());
            ps.setInt(4, chirie.getClient());
            ps.setInt(5, chirie.getPrice());
            ps.setInt(6, chirie.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void newRepair(Reparatie reparatie){
        String query = "INSERT INTO `reparatii`(`drone_id`, `descriere`, `cost`) VALUES (?,?,'-1')";
        String query2 = "UPDATE `drones` SET `status`='în așteptare' WHERE id = " + reparatie.getDrone_id();
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            PreparedStatement ps2 = conn.prepareStatement(query2);
        ){
            ps.setInt(1, reparatie.getDrone_id());
            ps.setString(2, reparatie.getDescriere());
            
            ps.executeUpdate();
            ps2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDroneStatus(int drone_id, String status){
        String query = "UPDATE drones SET status = ? WHERE id = ?";
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query)
        ){
            ps.setString(1, status);
            ps.setInt(2, drone_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDroneStatusAndRepairCost(int drone_id, String status, int price){
        String query = "UPDATE drones SET status = ? WHERE id = ?";
        String query2 = "UPDATE reparatii set cost = ? WHERE drone_id = ?";
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            PreparedStatement ps2 = conn.prepareStatement(query2)
        ){
            ps.setString(1, status);
            ps.setInt(2, drone_id);

            ps2.setInt(1, price);
            ps2.setInt(2, drone_id);

            ps.executeUpdate();
            ps2.executeUpdate();

            Database.reloadDrones();
            Database.reloadReparatii();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteRepair(int drone_id){
        String query = "DELETE FROM reparatii WHERE drone_id = "+drone_id;

        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(query)
        ){
            ps.executeUpdate();

            Database.reloadReparatii();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
