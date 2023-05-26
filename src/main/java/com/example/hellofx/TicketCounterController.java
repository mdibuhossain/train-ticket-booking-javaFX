package com.example.hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TicketCounterController implements Initializable {
    @FXML
    private GridPane seatGrid;
    @FXML
    private ComboBox<String> dropDownFrom;
    @FXML
    private ComboBox<String> dropDownTo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dropDownFrom.getItems().addAll("Dhaka", "Khulna", "Chattogram", "Sylhet", "Rangpur", "Gopalganj");
        dropDownTo.getItems().addAll("Dhaka", "Khulna", "Chattogram", "Sylhet", "Rangpur", "Gopalganj");
        int cnt = 1;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 5; j++) {
                if (j == 3) continue;
                Button seat = new Button();
                seat.setText(Integer.toString(cnt++));
                seat.setPrefSize(30, 30);
                if (cnt % 3 == 0) {
//                    seat.setStyle("-fx-background-color: red;");
                    seat.setDisable(true);
                }
                else
                    seat.setStyle("-fx-background-color: lime;");
//                ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/available_seat.png"))));
//                imageView.setFitWidth(30);
//                imageView.setFitHeight(30);
//                GridPane.setConstraints(seat, i - 1, j);
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
}
