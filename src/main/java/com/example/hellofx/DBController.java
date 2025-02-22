package com.example.hellofx;

import com.example.hellofx.models.User;

import java.sql.*;
import java.util.List;

import static com.example.hellofx.RowMapper.userMapper;

public class DBController {

    public static Connection connection;
    public static Statement statement;

    public static void CreateAdmin() {
        try {
            statement.executeUpdate("""
                        INSERT IGNORE INTO users(full_name, email, password, phone_number, address, role)
                        VALUES("Admin", "admin@email.com", "admin1234", "01911111111", "", "admin")
                    """);
        } catch (Exception e) {
            System.out.println("DATABASE INITIALIZATION ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void initializeDatabase() {
        try {
            // Connect to MySQL server
            String baseUri = "jdbc:mysql://localhost:3306/";
            String username = "root";
            String password = "12345678";

            connection = DriverManager.getConnection(baseUri, username, password);
            statement = connection.createStatement();

            // Create database
            String createDatabaseSQL = """
                        CREATE DATABASE IF NOT EXISTS train_booking_system
                        CHARACTER SET utf8mb4
                        COLLATE utf8mb4_unicode_ci
                    """;
            statement.executeUpdate(createDatabaseSQL);
            statement.executeUpdate("USE train_booking_system");

            // Create users table
            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                      user_id int NOT NULL AUTO_INCREMENT,
                      full_name varchar(100) NOT NULL,
                      email varchar(100) NOT NULL,
                      password varchar(50) NOT NULL,
                      phone_number varchar(20) NOT NULL,
                      address varchar(200) NOT NULL,
                      role enum('admin','user') NOT NULL DEFAULT 'user',
                      PRIMARY KEY (user_id),
                      UNIQUE KEY email_UNIQUE (email)
                    );
                    """;
            statement.executeUpdate(createUsersTable);

            // Create stations table
            String createStationsTable = """
                    CREATE TABLE IF NOT EXISTS stations (
                      station_id int NOT NULL AUTO_INCREMENT,
                      station_name varchar(100) NOT NULL,
                      PRIMARY KEY (station_id),
                      UNIQUE KEY station_name_UNIQUE (station_name)
                    );
                    """;
            statement.executeUpdate(createStationsTable);

            String createTrain = """
                    CREATE TABLE IF NOT EXISTS trains (
                      train_id int NOT NULL AUTO_INCREMENT,
                      train_name varchar(50) NOT NULL,
                      PRIMARY KEY (train_id),
                      UNIQUE KEY train_name (train_name)
                    );
                    """;
            statement.executeUpdate(createTrain);

            // Create trips table
            String createTripsTable = """
                    CREATE TABLE IF NOT EXISTS trips (
                      trip_id int NOT NULL AUTO_INCREMENT,
                      station_from varchar(100) NOT NULL,
                      station_to varchar(100) NOT NULL,
                      train_name varchar(50) DEFAULT NULL,
                      available_seats int NOT NULL DEFAULT '50',
                      journey_date varchar(15) NOT NULL,
                      journey_time varchar(15) NOT NULL,
                      PRIMARY KEY (trip_id),
                      KEY trips_ibfk_1_idx (station_from),
                      KEY trips_ibfk_2 (station_to),
                      CONSTRAINT trips_ibfk_1 FOREIGN KEY (station_from) REFERENCES stations (station_name),
                      CONSTRAINT trips_ibfk_2 FOREIGN KEY (station_to) REFERENCES stations (station_name)
                    );
                    """;
            statement.executeUpdate(createTripsTable);

            // Create booking table
            String createBookingTable = """                    
                    CREATE TABLE IF NOT EXISTS booking (
                      booking_id int NOT NULL AUTO_INCREMENT,
                      user_id int DEFAULT NULL,
                      trip_id int DEFAULT NULL,
                      seat_number int DEFAULT NULL,
                      booking_time varchar(100) DEFAULT NULL,
                      PRIMARY KEY (booking_id),
                      KEY user_id (user_id),
                      KEY trip_id (trip_id),
                      CONSTRAINT booking_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (user_id),
                      CONSTRAINT booking_ibfk_2 FOREIGN KEY (trip_id) REFERENCES trips (trip_id)
                    );
                    """;
            statement.executeUpdate(createBookingTable);

            // Create seat_booked table
            String createSeatBookedTable = """
                    CREATE TABLE IF NOT EXISTS seat_booked (
                      seat_id int NOT NULL AUTO_INCREMENT,
                      trip_id int DEFAULT NULL,
                      booking_id int DEFAULT NULL,
                      seat_number int DEFAULT NULL,
                      PRIMARY KEY (seat_id),
                      KEY trip_id (trip_id),
                      KEY booking_id (booking_id),
                      CONSTRAINT seat_booked_ibfk_1 FOREIGN KEY (trip_id) REFERENCES trips (trip_id),
                      CONSTRAINT seat_booked_ibfk_2 FOREIGN KEY (booking_id) REFERENCES booking (booking_id)
                    );
                    """;
            statement.executeUpdate(createSeatBookedTable);
            CreateAdmin();
        } catch (SQLException e) {
            System.out.println("DATABASE INITIALIZATION ERROR: " + e.getMessage());
        }
    }

    public static void getConnection() {
        try {
            String uri = "jdbc:mysql://localhost:3306/";
            String username = "root";
            String password = "12345678";
            connection = DriverManager.getConnection(uri, username, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("SERVER ERROR");
        }
    }

    public static void closeConnection() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException ignored) {
            System.out.println("Connection close problem");
        }
    }

    public static void main(String[] args) throws SQLException {
//        initializeDatabase();
//        getConnection();
//        String query = """
//                    SELECT *
//                    FROM users
//                """;
//        ResultSet resultSet = statement.executeQuery(query);
//        List<User> users = userMapper(resultSet);
//        for (int i = 0; i < users.size(); i++) {
//            System.out.println(users.get(i).getFull_name());
//        }
//        userMapper(resultSet);
    }

}
