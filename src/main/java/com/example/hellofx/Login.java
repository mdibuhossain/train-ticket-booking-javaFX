package com.example.hellofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Login extends Application {

    public static final int INIT_WIDTH = 840;
    public static final int INIT_HEIGHT = 580;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        primaryStage.setTitle("Train Ticket Booking System");
        primaryStage.setScene(new Scene(root, INIT_WIDTH+100, INIT_HEIGHT+100));
        primaryStage.setMinWidth(INIT_WIDTH);
        primaryStage.setMinHeight(INIT_HEIGHT);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
