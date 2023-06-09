package com.example.hellofx;

import com.example.hellofx.models.Station;
import com.example.hellofx.models.Trip;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    @FXML
    private TextField stationNameField;
    @FXML
    private ListView<String> stationListView;
    @FXML
    private ComboBox<String> dropDownFrom;
    @FXML
    private ComboBox<String> dropDownTo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> time;
    @FXML
    private TableView<Trip> tripTable;
    @FXML
    private TableColumn<Trip, String> fromColumn;
    @FXML
    private TableColumn<Trip, String> toColumn;
    @FXML
    private TableColumn<Trip, String> dateColumn;
    @FXML
    private TableColumn<Trip, String> timeColumn;
    private ObservableList<Trip> tripList;
    private List<Station> stations;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initStation();
        initTripTable();
        initDepartureTime();
        formatDatePicker();

    }

    private void initTripTable() {
        fromColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("to"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("time"));
        tripList = tripTable.getItems();
        tripTable.setItems(tripList);
    }

    private void initDepartureTime() {
        time.getItems().addAll("6:00 AM", "8:00 AM", "10:00 AM", "12:00 PM", "2:00 PM", "4:00 PM", "6:00 PM", "8:00 PM", "10:00 PM", "12:00 AM", "2:00 AM", "4:00 AM");
    }

    private void formatDatePicker() {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        datePicker.setConverter(converter);
    }

    @FXML
    private void initStation() {
        String stationSQL = "SELECT * FROM stations ORDER BY station_name ASC";
        try {
            ResultSet resultSet = DBController.statement.executeQuery(stationSQL);
            stations = RowMapper.stationMapper(resultSet);
            dropDownFrom.getItems().clear();
            dropDownTo.getItems().clear();
            stationListView.getItems().clear();
            for (Station station : stations) {
                dropDownFrom.getItems().add(station.getStation_name());
                dropDownTo.getItems().add(station.getStation_name());
                stationListView.getItems().add(station.getStation_name());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void addStation(ActionEvent event) {
        String stationName = stationNameField.getText();
        String sql = "INSERT INTO stations(station_name) VALUE('" + stationName + "')";
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            int createStation = preparedStatement.executeUpdate();
            if (createStation > 0) {
                initStation();
                stationNameField.setText("");
            } else {
                System.out.println("NOT created");
            }
        } catch (Exception ignore) {

        }
    }

    @FXML
    private void removeStation() {
        int selectedID = stationListView.getSelectionModel().getSelectedIndex();
        String selectedStation = stationListView.getItems().get(selectedID);
        String sql = String.format("DELETE FROM stations WHERE station_name = '%s'", selectedStation);
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            int isDeleteStation = preparedStatement.executeUpdate();
            if (isDeleteStation > 0) {
                stationListView.getItems().remove(selectedID);
            }
        } catch (Exception ignore) {

        }
    }

    @FXML
    private void addTrip() {
        String fromText = dropDownFrom.getValue();
        String toText = dropDownTo.getValue();
        LocalDate selectedDate = datePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = selectedDate.format(formatter);
        String journeyTime = time.getValue();
        System.out.println(fromText + " " + toText + " " + date + " " + journeyTime);
        String sql = "INSERT INTO trips(station_from, station_to, journey_date, journey_time) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            preparedStatement.setString(1, fromText);
            preparedStatement.setString(2, toText);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, journeyTime);
            int insertTrip = preparedStatement.executeUpdate();
            if (insertTrip > 0) {
                tripList.add(new Trip(fromText, toText, date, journeyTime));
            }
        } catch (Exception ignore) {
            System.out.println(ignore.getMessage());
        }
    }
}
