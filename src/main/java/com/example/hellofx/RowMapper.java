package com.example.hellofx;

import com.example.hellofx.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapper {
    public static List<User> userRowMapper(ResultSet resultSet) {
        List<User> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String full_name = resultSet.getString("full_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String phone_number = resultSet.getString("phone_number");
                String address = resultSet.getString("address");
                User user = new User(user_id, full_name, email, phone_number, address);
                users.add(user);
            }
            return users;
        } catch (SQLException ignored) {
            System.out.println("Something went wrong!");
            return users;
        }
    }
}
