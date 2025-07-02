package com.example.hellofx;

import com.example.hellofx.models.Station;
import com.example.hellofx.models.Trip;
import com.example.hellofx.models.User;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

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
    private TableColumn<Trip, String> trainColumn;
    @FXML
    private TableColumn<Trip, Integer> seatColumn;
    @FXML
    private TableColumn<Trip, String> dateColumn;
    @FXML
    private TableColumn<Trip, String> timeColumn;
    @FXML
    private Text booked_train;
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
    private int booked_trip_id;
    private ObservableList<Trip> tripList;
    private List<Station> stations;
    private User user = new User();
    private String seatsBooked = "";
    private ButtonType downloadButton;
    Set<Integer> selectedSeats = new TreeSet<>();
    Alert bookedConfirmation = new Alert(Alert.AlertType.INFORMATION);
    int selectTripID = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initStation();
        initTripTable();
        formatDatePicker();
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
        trainColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("train"));
        seatColumn.setCellValueFactory(new PropertyValueFactory<Trip, Integer>("available_seats"));
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
            tripList = RowMapper.tripMapper(resultSet);
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

    private void initBookingInfo(String from, String to, String train, String date, String time) {
        booked_name.setText(user.getFull_name());
        booked_train.setText(train);
        booked_from.setText(from);
        booked_to.setText(to);
        booked_date.setText(date);
        booked_time.setText(time);
        booked_phoneNumber.setText(user.getPhone_number());
        bookedConfirmation.setTitle("Receipt");
        bookedConfirmation.setHeaderText("Seat booked successful");
        bookedConfirmation.setContentText(String.format(
                "%-15s %2s %-18s\n" +
                        "%-15s %2s %-18s\n" +
                        "%-15s %2s %-18s\n" +
                        "%-15s %2s %-18s\n" +
                        "%-15s %2s %-18s\n" +
                        "%-15s %2s %-18s\n" +
                        "%-15s %2s %-18s\n" +
                        "%-15s %2s %-18s\n",
                "Name", ":", user.getFull_name(),
                "Train", ":", train,
                "From", ":", from,
                "To", ":", to,
                "Seat", ":", seatsBooked,
                "Date", ":", date,
                "Time", ":", time,
                "Contact", ":", user.getPhone_number()
        ));
        downloadButton = new ButtonType("Download", ButtonBar.ButtonData.OK_DONE);
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        bookedConfirmation.getButtonTypes().setAll(downloadButton, closeButton);
    }

    @FXML
    private void tripSeatFetch() {
        int selectedID = tripTable.getSelectionModel().getSelectedIndex();
        if (tripTable.getSelectionModel().getSelectedItem() != null) {
            String from = tripList.get(selectedID).getFrom();
            String to = tripList.get(selectedID).getTo();
            String date = tripList.get(selectedID).getDate();
            String time = tripList.get(selectedID).getTime();
            String train = tripList.get(selectedID).getTrain();
            booked_trip_id = tripList.get(selectedID).getTrip_id();
            initSeats();
            initBookingInfo(from, to, train, date, time);
        }
    }

    private void initSeats() {
        seatGrid.getChildren().clear();
        selectedSeats.clear();
        String sql = "SELECT * FROM booking WHERE trip_id='" + booked_trip_id + "'";
        try {
            ResultSet resultSet = DBController.statement.executeQuery(sql);
            Set<Integer> bookedSeatList = new TreeSet<>(RowMapper.seatMapper(resultSet));
            int cnt = 1;
            for (int i = 1; i <= 10; i++) {
                for (int j = 1; j <= 6; j++) {
                    if (j == 3) continue;
                    Button seat = new Button();
                    seat.setText(Integer.toString(cnt));
                    seat.setPrefSize(30, 30);
                    if (bookedSeatList.contains(cnt)) {
                        seat.setStyle("-fx-background-color: #e8e8e8;");
                        seat.setDisable(true);
                    } else seat.setStyle("-fx-background-color: lime;");
                    GridPane.setConstraints(seat, j - 1, i - 1);
                    GridPane.setHalignment(seat, HPos.CENTER);
                    GridPane.setValignment(seat, VPos.CENTER);
                    seatGrid.getChildren().add(seat);
                    cnt++;
                }
            }
            for (Node node : seatGrid.getChildren()) {
                if (node instanceof Button button) {
                    button.addEventHandler(ActionEvent.ACTION, event -> {
                        if (button.getStyle().equals("-fx-background-color: #fc99ff;")) {
                            selectedSeats.remove(Integer.valueOf(button.getText()));
                            button.setStyle("-fx-background-color: lime;");
                        } else {
                            selectedSeats.add(Integer.valueOf(button.getText()));
                            System.out.println(selectedSeats.toString());
                            button.setStyle("-fx-background-color: #fc99ff;");
                        }
                        StringBuilder dispSeat = new StringBuilder();
                        for (int seat : selectedSeats) {
                            dispSeat.append(seat).append(", ");
                        }
                        if (dispSeat.length() > 1)
                            dispSeat = new StringBuilder(dispSeat.substring(0, dispSeat.length() - 2));
                        booked_seats.setText(dispSeat.toString());
                        seatsBooked = dispSeat.toString();
                    });
                }
            }
        } catch (Exception ignore) {
            System.out.println(ignore.getMessage());
        }
    }

    @FXML
    private void handleSeatBookAction() {
        int isInsertTrip = 0;
        for (int seat : selectedSeats) {
            String sql = "INSERT INTO booking(user_id, trip_id, seat_number, booking_time) VALUES(?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
                preparedStatement.setInt(1, user.getUser_id());
                preparedStatement.setInt(2, booked_trip_id);
                preparedStatement.setInt(3, seat);
                preparedStatement.setString(4, String.valueOf(new Date()));
                isInsertTrip = preparedStatement.executeUpdate();
            } catch (Exception ignore) {
                System.out.println(ignore.getMessage());
                break;
            }
        }
        if (isInsertTrip > 0) {
            if (selectedSeats.size() > 0) {
                if (tripTable.getSelectionModel().getSelectedIndex() >= 0) {
                    selectTripID = tripTable.getSelectionModel().getSelectedIndex();
                }
                System.out.println(selectTripID);
                int beforeSeats = tripList.get(selectTripID).getAvailable_seats();
                System.out.println(beforeSeats);
                System.out.println(selectedSeats.size());

                String updateSQL = "UPDATE trips SET available_seats=? WHERE trip_id=?";
                try {
                    PreparedStatement preparedStatement = DBController.connection.prepareStatement(updateSQL);
                    preparedStatement.setInt(1, (beforeSeats - selectedSeats.size()));
                    preparedStatement.setInt(2, booked_trip_id);
                    int isTripUpdated = preparedStatement.executeUpdate();
                    if (isTripUpdated > 0) {
                        searchTrip();
//                        tripTable.getSelectionModel().getSelectedItem().setAvailable_seats(beforeSeats - selectedSeats.size());
                    }
                    Optional<ButtonType> result = bookedConfirmation.showAndWait();
                    if (result.isPresent() && result.get() == downloadButton) {
                        downloadReceiptAsPDF(booked_from.getText(), booked_to.getText(), booked_train.getText(), booked_date.getText(), booked_time.getText(), seatsBooked);
                    }
                } catch (Exception ignore) {
                    System.out.println(ignore.getMessage());
                }
            }
            initSeats();
        }
    }

    public void downloadReceiptAsPDF(String from, String to, String train, String date, String time, String seatsBooked) {
        Document document = new Document();
        try {
            // Save location
            File pdfFile = new File("BookingReceipt.pdf");
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 12);

            Paragraph title = new Paragraph("Booking Receipt\n\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            PdfPTable table = new PdfPTable(2);
            table.setWidths(new int[]{1, 3});
            table.setWidthPercentage(100);

            addRow(table, "Name", user.getFull_name(), contentFont);
            addRow(table, "Train", train, contentFont);
            addRow(table, "From", from, contentFont);
            addRow(table, "To", to, contentFont);
            addRow(table, "Seats", seatsBooked, contentFont);
            addRow(table, "Date", date, contentFont);
            addRow(table, "Time", time, contentFont);
            addRow(table, "Contact", user.getPhone_number(), contentFont);

            document.add(table);
            document.close();

            // ✅ Open the file
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, font));
        PdfPCell cell2 = new PdfPCell(new Phrase(value, font));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
    }
}
