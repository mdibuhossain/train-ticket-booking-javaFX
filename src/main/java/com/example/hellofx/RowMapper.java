package com.example.hellofx;

import com.example.hellofx.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static List<Train> trainMapper(ResultSet resultSet) {
        List<Train> trains = new ArrayList<>();
        try {
            while (resultSet.next()) {
                trains.add(new Train(
                        resultSet.getInt("train_id"),
                        resultSet.getString("train_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trains;
    }

    public static List<Station> stationMapper(ResultSet resultSet) {
        List<Station> stations = new ArrayList<>();
        try {
            while (resultSet.next()) {
                stations.add(new Station(
                        resultSet.getInt("station_id"),
                        resultSet.getString("station_name"
                        )
                ));
            }
        } catch (SQLException ignore) {
            System.out.println("Something went wrong!");
        }
        return stations;
    }

    public static ObservableList<Trip> tripMapper(ResultSet resultSet) {
        ObservableList<Trip> trips = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                trips.add(new Trip(
                                resultSet.getInt("trip_id"),
                                resultSet.getString("station_from"),
                                resultSet.getString("station_to"),
                                resultSet.getString("train_name"),
                                resultSet.getString("journey_date"),
                                resultSet.getString("journey_time")
                        )
                );
            }
        } catch (SQLException ignore) {
            System.out.println("Something went wrong!");
        }
        return trips;
    }

    public static Set<Integer> seatMapper(ResultSet resultSet) {
        Set<Integer> seats = new HashSet<>();
        try {
            while (resultSet.next()) {
                seats.add(resultSet.getInt("seat_number"));
            }
        } catch (Exception ignore) {
            System.out.println(ignore.getMessage());
        }
        return seats;
    }

    public static List<Booking> bookingMapper(ResultSet resultSet) {
        List<Booking> bookingList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                bookingList.add(new Booking(
                        resultSet.getInt("booking_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("trip_id"),
                        resultSet.getInt("seat_number"),
                        resultSet.getString("booking_time")
                ));
            }
        } catch (Exception ignore) {
            System.out.println(ignore.getMessage());
        }
        return bookingList;
    }
}
