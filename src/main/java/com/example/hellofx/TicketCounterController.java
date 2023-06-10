package com.example.hellofx;

import com.example.hellofx.models.Station;
import com.example.hellofx.models.Trip;
import com.example.hellofx.models.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class TicketCounterController implements Initializable {
    @FXML
    private GridPane seatGrid;
    @FXML
    private ComboBox<String> dropDownFrom;
    @FXML
    private ComboBox<String> dropDownTo;
    @FXML
    private DatePicker datePicker;
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
    @FXML
    private Text booked_date;
    @FXML
    private Text booked_from;
    @FXML
    private Text booked_name;
    @FXML
    private Text booked_seats;
    @FXML
    private Text booked_time;
    @FXML
    private Text booked_to;
    @FXML
    private Text booked_phoneNumber;
    private ObservableList<Trip> tripList;
    private List<Station> stations;
    private User user = new User();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initStation();
        initTripTable();
        formatDatePicker();
        int cnt = 1;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 6; j++) {
                if (j == 3) continue;
                Button seat = new Button();
                seat.setText(Integer.toString(cnt++));
                seat.setPrefSize(30, 30);
                if (cnt % 3 == 0) {
//                    seat.setStyle("-fx-background-color: red;");
                    seat.setDisable(true);
                } else seat.setStyle("-fx-background-color: lime;");
                GridPane.setConstraints(seat, j - 1, i - 1);
                GridPane.setHalignment(seat, HPos.CENTER);
                GridPane.setValignment(seat, VPos.CENTER);
                seatGrid.getChildren().add(seat);
            }
        }
        for (Node node : seatGrid.getChildren()) {
            if (node instanceof Button button) {
                button.addEventHandler(ActionEvent.ACTION, event -> {
                    if (button.getStyle().equals("-fx-background-color: #fc99ff;")) {
                        button.setStyle("-fx-background-color: lime;");
                    } else {
                        button.setStyle("-fx-background-color: #fc99ff;");
                    }
                });
            }
        }
    }

    public void setUser(User tmpUser) {
        user = new User(tmpUser);
//        user.setUser_id(tmpUser.getUser_id());
//        user.setFull_name(tmpUser.getFull_name());
//        user.setEmail(tmpUser.getEmail());
//        user.setPassword(tmpUser.getPassword());
//        user.setPhone_number(tmpUser.getPhone_number());
//        user.setAddress(tmpUser.getAddress());
//        user.setRole(tmpUser.getRole());
    }

    private void initTripTable() {
        fromColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("to"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("time"));
    }

    private void fetchTripTableData(String fromText, String toText, String date) {
        String sql = "SELECT * FROM trips WHERE station_from=? and station_to=? and journey_date=?";
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            preparedStatement.setString(1, fromText);
            preparedStatement.setString(2, toText);
            preparedStatement.setString(3, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            tripList = RowMapper.TripMapper(resultSet);
        } catch (Exception ignore) {
            System.out.println(ignore.getMessage());
            tripList = tripTable.getItems();
        }
        tripTable.setItems(tripList);
    }

    private void initStation() {
        String stationSQL = "SELECT * FROM stations ORDER BY station_name ASC";
        try {
            ResultSet resultSet = DBController.statement.executeQuery(stationSQL);
            stations = RowMapper.stationMapper(resultSet);
            for (Station station : stations) {
                dropDownFrom.getItems().add(station.getStation_name());
                dropDownTo.getItems().add(station.getStation_name());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    private void searchTrip() {
        String fromText = dropDownFrom.getValue();
        String toText = dropDownTo.getValue();
        LocalDate selectedDate = datePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = selectedDate.format(formatter);
        fetchTripTableData(fromText, toText, date);
    }

    private void initBookingInfo(String from, String to, String date, String time) {
        booked_name.setText(user.getFull_name());
        booked_from.setText(from);
        booked_to.setText(to);
        booked_date.setText(date);
        booked_time.setText(time);
        booked_phoneNumber.setText(user.getPhone_number());
    }

    @FXML
    private void tripSeatFetch() {
        int selectedID = tripTable.getSelectionModel().getSelectedIndex();
        String from = tripList.get(selectedID).getFrom();
        String to = tripList.get(selectedID).getTo();
        String date = tripList.get(selectedID).getDate();
        String time = tripList.get(selectedID).getTime();
        initBookingInfo(from, to, date, time);
        System.out.println("hello");
    }


}
