package com.example.hellofx;

import com.example.hellofx.models.User;

import java.sql.*;
import java.util.List;

import static com.example.hellofx.RowMapper.userMapper;

public class DBController {

    public static Connection connection;
    public static Statement statement;

    public static void getConnection() {
        try {
            String uri = "jdbc:mysql://localhost:3306/train_booking_system";
            String username = "root";
            String password = "root";
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
        getConnection();
        String query = """
                    SELECT *
                    FROM users
                """;
        ResultSet resultSet = statement.executeQuery(query);
        List<User> users = userMapper(resultSet);
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i).getFull_name());
        }
        userMapper(resultSet);
    }

}
