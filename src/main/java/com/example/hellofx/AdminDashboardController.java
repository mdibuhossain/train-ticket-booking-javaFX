package com.example.hellofx;

import com.example.hellofx.models.Station;
import com.example.hellofx.models.Train;
import com.example.hellofx.models.Trip;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

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
    private ListView<String> trainListView;
    @FXML
    private ComboBox<String> dropDownFrom;
    @FXML
    private ComboBox<String> dropDownTo;
    @FXML
    private ComboBox<String> dropDownTrain;
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
    private TableColumn<Trip, String> toTrain;
    @FXML
    private TableColumn<Trip, String> dateColumn;
    @FXML
    private TableColumn<Trip, String> timeColumn;
    @FXML
    private TextField trainNameField;
    private ObservableList<Trip> tripList;
    private List<Station> stations;
    private List<Train> trains;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initStation();
        initTripTable();
        initTrain();
        initDepartureTime();
        formatDatePicker();

    }

    private void initTripTable() {
        fromColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("to"));
        toTrain.setCellValueFactory(new PropertyValueFactory<Trip, String>("train"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("time"));
        fetchTripTableData();
    }

    @FXML
    private void fetchTripTableData() {
        String sql = "SELECT * FROM trips";
        try {
            ResultSet resultSet = DBController.statement.executeQuery(sql);
            tripList = RowMapper.tripMapper(resultSet);
        } catch (Exception ignore) {
            tripList = tripTable.getItems();
        }
        tripTable.setItems(tripList);
    }

    private void initDepartureTime() {
        time.getItems().addAll("6:00 AM", "8:00 AM", "10:00 AM", "12:00 PM", "2:00 PM", "4:00 PM", "6:00 PM", "8:00 PM", "10:00 PM", "12:00 AM", "2:00 AM", "4:00 AM");
    }

    private void formatDatePicker() {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
    private void initTrain() {
        String trainSQL = "SELECT * FROM trains ORDER BY train_name ASC";
        try {
            ResultSet resultSet = DBController.statement.executeQuery(trainSQL);
            trains = RowMapper.trainMapper(resultSet);
            dropDownTrain.getItems().clear();
            trainListView.getItems().clear();
            for (Train train : trains) {
                dropDownTrain.getItems().add(train.getTrain_name());
                trainListView.getItems().add(train.getTrain_name());
            }
        } catch (SQLException ignore) {

        }
    }

    @FXML
    private void addTrain() {
        String trainName = trainNameField.getText();
        String sql = "INSERT INTO trains(train_name) VALUE('" + trainName + "')";
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            int createTrain = preparedStatement.executeUpdate();
            if (createTrain > 0) {
                initTrain();
                stationNameField.setText("");
            } else {
                System.out.println("NOT created");
            }
        } catch (SQLException ignore) {

        }
    }

    @FXML
    private void deleteTrain() {
        int selectedTrainID = trainListView.getSelectionModel().getSelectedIndex();
        String selectedTrain = trainListView.getItems().get(selectedTrainID);
        String sql = String.format("DELETE FROM trains WHERE train_name='%s'", selectedTrain);
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            int isTrainDeleted = preparedStatement.executeUpdate();
            if (isTrainDeleted > 0) {
                trainListView.getItems().remove(selectedTrainID);
            }
        } catch (SQLException ignore) {

        }
    }

    @FXML
    void handleTrainClick(MouseEvent event) {
        String selectedItem = trainListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            trainNameField.setText(selectedItem);
        }
    }

    @FXML
    private void updateTrain() {
        int selectedTrainID = trainListView.getSelectionModel().getSelectedIndex();
        String selectedTrain = trainListView.getItems().get(selectedTrainID);
        String sql = String.format("UPDATE trains SET train_name='%s' WHERE train_name='%s'", trainNameField.getText(), selectedTrain);
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            int isTrainUpdated = preparedStatement.executeUpdate();
            if (isTrainUpdated > 0) {
                trainListView.getItems().set(selectedTrainID, trainNameField.getText());
            }
        } catch (SQLException ignore) {

        }
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
        } catch (SQLException ignore) {
            System.out.println(ignore.getMessage());
//            throw new RuntimeException(e);
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
            System.out.println(ignore.getMessage());
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
            System.out.println(ignore.getMessage());
        }
    }

    @FXML
    private void addTrip() {
        String fromText = dropDownFrom.getValue();
        String toText = dropDownTo.getValue();
        String selectedTrain = dropDownTrain.getValue();
        LocalDate selectedDate = datePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = selectedDate.format(formatter);
        String journeyTime = time.getValue();
        String sql = "INSERT INTO trips(station_from, station_to, train_name, journey_date, journey_time) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            preparedStatement.setString(1, fromText);
            preparedStatement.setString(2, toText);
            preparedStatement.setString(3, selectedTrain);
            preparedStatement.setString(4, date);
            preparedStatement.setString(5, journeyTime);
            int insertTrip = preparedStatement.executeUpdate();
            if (insertTrip > 0) {
                tripList.add(new Trip(fromText, toText, selectedTrain, date, journeyTime));
            }
        } catch (Exception ignore) {
            System.out.println(ignore.getMessage());
        }
    }

    @FXML
    private void removeTrip() {
        int selectedID = tripTable.getSelectionModel().getSelectedIndex();
        String trip_id = String.valueOf(tripList.get(selectedID).getTrip_id());
        String sql = "DELETE FROM trips WHERE trip_id = '" + trip_id + "'";
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            int isDeleteTrip = preparedStatement.executeUpdate();
            if (isDeleteTrip > 0) {
                tripList.remove(selectedID);
            }
        } catch (Exception ignore) {
            System.out.println(ignore.getMessage());
        }
    }

    @FXML
    void handleTripClick(MouseEvent event) {
        int selectedID = tripTable.getSelectionModel().getSelectedIndex();
        if (tripTable.getSelectionModel().getSelectedItem() != null) {
            String trip_id = String.valueOf(tripList.get(selectedID).getTrip_id());
            dropDownTrain.setValue(tripList.get(selectedID).getTrain());
            dropDownFrom.setValue(tripList.get(selectedID).getFrom());
            dropDownTo.setValue(tripList.get(selectedID).getTo());
            datePicker.setValue(LocalDate.parse(tripList.get(selectedID).getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            time.setValue(tripList.get(selectedID).getTime());
        }
    }

    @FXML
    private void updateTrip() {
        int selectedID = tripTable.getSelectionModel().getSelectedIndex();
        String trip_id = String.valueOf(tripList.get(selectedID).getTrip_id());
        String fromText = dropDownFrom.getValue();
        String toText = dropDownTo.getValue();
        String selectedTrain = dropDownTrain.getValue();
        LocalDate selectedDate = datePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = selectedDate.format(formatter);
        String journeyTime = time.getValue();
        String sql = "UPDATE trips SET station_from=?, station_to=?, train_name=?, journey_date=?, journey_time=? WHERE trip_id=?";
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            preparedStatement.setString(1, fromText);
            preparedStatement.setString(2, toText);
            preparedStatement.setString(3, selectedTrain);
            preparedStatement.setString(4, date);
            preparedStatement.setString(5, journeyTime);
            preparedStatement.setString(6, trip_id);
            int isTripUpdated = preparedStatement.executeUpdate();
            if (isTripUpdated > 0) {
                tripList.set(selectedID, new Trip(Integer.parseInt(trip_id), fromText, toText, selectedTrain, date, journeyTime));
            }
        } catch (Exception ignore) {
            System.out.println(ignore.getMessage());
        }
    }
}
