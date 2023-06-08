package com.example.hellofx;

import com.example.hellofx.models.Station;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TextField time;
    private List<Station> stations;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initStation();
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
    private void addStation() {
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
}
