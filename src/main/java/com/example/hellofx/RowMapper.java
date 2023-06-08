package com.example.hellofx;

import com.example.hellofx.models.Station;
import com.example.hellofx.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapper {
    public static List<User> userMapper(ResultSet resultSet) {
        List<User> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String full_name = resultSet.getString("full_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String phone_number = resultSet.getString("phone_number");
                String address = resultSet.getString("address");
                String role = resultSet.getString("role");
                User user = new User(user_id, full_name, email, password, phone_number, address, role);
                users.add(user);
            }
            return users;
        } catch (SQLException ignored) {
            System.out.println("Something went wrong!");
            return users;
        }
    }

    public static List<Station> stationMapper(ResultSet resultSet) {
        List<Station> stations = new ArrayList<>();
        try {
            while (resultSet.next()) {
                stations.add(new Station(resultSet.getInt("station_id"), resultSet.getString("station_name")));
            }
        } catch (SQLException ignore) {
            System.out.println("Something went wrong!");
        }
        return stations;
    }
}
