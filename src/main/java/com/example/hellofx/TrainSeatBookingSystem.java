package com.example.hellofx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TrainSeatBookingSystem extends Application {

    private boolean[] seats = new boolean[60]; // 60 seats in the train
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Train Seat Booking System");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Add "from" and "to" labels and text fields
        Label fromLabel = new Label("From:");
        grid.add(fromLabel, 0, 0);
        Label toLabel = new Label("To:");
        grid.add(toLabel, 1, 0);
        Label fromText = new Label("New York");
        grid.add(fromText, 0, 1);
        Label toText = new Label("Boston");
        grid.add(toText, 1, 1);

        // Add seat buttons
        int row = 2, col = 0;
        for (int i = 0; i < 60; i++) {
            Button button = new Button(String.valueOf(i + 1));
            button.setPrefSize(50, 50);
            if (i % 6 == 0) {
                row++;
                col = 0;
            }
            grid.add(button, col++, row);
            int seatNumber = i;
            button.setOnAction(e -> bookSeat(seatNumber, button));
        }

        // Add status label
        statusLabel = new Label("Please select a seat");
        grid.add(statusLabel, 0, row + 1);

        Scene scene = new Scene(grid, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void bookSeat(int seatNumber, Button button) {
        if (seats[seatNumber]) {
            seats[seatNumber] = false;
            button.setStyle(null);
//            statusLabel.setText("Seat " + (seatNumber + 1) + " is already booked");
        } else {
            seats[seatNumber] = true;
            button.setStyle("-fx-background-color: #00FF00;"); // Green color for booked seat
            statusLabel.setText("Seat " + (seatNumber + 1) + " has been booked");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
