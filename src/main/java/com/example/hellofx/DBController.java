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
                            user_id INT PRIMARY KEY AUTO_INCREMENT,
                            full_name VARCHAR(100) NOT NULL,
                            email VARCHAR(100) NOT NULL,
                            password VARCHAR(50) NOT NULL,
                            phone_number VARCHAR(20) NOT NULL,
                            address VARCHAR(200) NOT NULL,
                            role ENUM('admin', 'user') DEFAULT 'user',
                            CONSTRAINT email_UNIQUE UNIQUE (email)
                        )
                    """;
            statement.executeUpdate(createUsersTable);

            // Create stations table
            String createStationsTable = """
                        CREATE TABLE IF NOT EXISTS stations (
                            station_id INT PRIMARY KEY AUTO_INCREMENT,
                            station_name VARCHAR(100) NOT NULL UNIQUE
                        )
                    """;
            statement.executeUpdate(createStationsTable);

            // Create trips table
            String createTripsTable = """
                        CREATE TABLE IF NOT EXISTS trips (
                            trip_id INT PRIMARY KEY AUTO_INCREMENT,
                            source_station_id INT NOT NULL,
                            destination_station_id INT NOT NULL,
                            trip_date DATE NOT NULL,
                            trip_time VARCHAR(20) NOT NULL,
                            FOREIGN KEY (source_station_id) REFERENCES stations(station_id),
                            FOREIGN KEY (destination_station_id) REFERENCES stations(station_id)
                        )
                    """;
            statement.executeUpdate(createTripsTable);

            // Create booking table
            String createBookingTable = """
                        CREATE TABLE IF NOT EXISTS booking (
                            booking_id INT PRIMARY KEY AUTO_INCREMENT,
                            user_id INT NOT NULL,
                            trip_id INT NOT NULL,
                            seat_number INT NOT NULL,
                            booking_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(user_id),
                            FOREIGN KEY (trip_id) REFERENCES trips(trip_id)
                        )
                    """;
            statement.executeUpdate(createBookingTable);

            // Create seat_booked table
            String createSeatBookedTable = """
                        CREATE TABLE IF NOT EXISTS seat_booked (
                            seat_id INT PRIMARY KEY AUTO_INCREMENT,
                            trip_id INT NOT NULL,
                            booking_id INT NOT NULL,
                            seat_number INT NOT NULL,
                            FOREIGN KEY (trip_id) REFERENCES trips(trip_id),
                            FOREIGN KEY (booking_id) REFERENCES booking(booking_id),
                            CONSTRAINT unique_seat_per_trip UNIQUE (trip_id, seat_number)
                        )
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
