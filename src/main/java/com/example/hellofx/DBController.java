package com.example.hellofx;

import java.sql.*;

public class DBController {

    public static Connection getConnection() throws SQLException {
        String uri = "jdbc:mysql://localhost:3306/sql_store";
        String username = "root";
        String password = "admin";
        return DriverManager.getConnection(uri, username, password);
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DBController.getConnection();
        String query = "SELECT * FROM customers";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        // Process the result set
        while (resultSet.next()) {
            // Access the data
            String columnValue = resultSet.getString("first_name");
            // Perform operations with the retrieved data
            System.out.println(columnValue);
        }
        // Close the connection and release resources
        resultSet.close();
        statement.close();
        connection.close();

    }

}
