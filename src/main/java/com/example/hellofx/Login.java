package com.example.hellofx;

import com.example.hellofx.models.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.example.hellofx.DBController.getConnection;

public class Login extends Application {

    public static Stage primaryStage;
    public static final int INIT_WIDTH = 840;
    public static final int INIT_HEIGHT = 580;

    public static void main(String[] args) {
        getConnection();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Login.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        primaryStage.setTitle("Train Ticket Booking System");
        primaryStage.setScene(new Scene(root, INIT_WIDTH + 100, INIT_HEIGHT + 100));
        primaryStage.setMinWidth(INIT_WIDTH);
        primaryStage.setMinHeight(INIT_HEIGHT);
        primaryStage.setResizable(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> Platform.exit());
    }

    public static void openDashboard(User tmpUser) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Login.class.getResource("ticket_counter.fxml")));
//            Parent root = FXMLLoader.load(Objects.requireNonNull(Login.class.getResource("ticket_counter.fxml")));
            Parent root = loader.load();
            TicketCounterController ticketCounterController = loader.getController();
            ticketCounterController.setUser(tmpUser);
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Dashboard");
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();
            primaryStage.hide();
            dashboardStage.setOnCloseRequest(windowEvent -> {
                primaryStage.show();
            });
        } catch (IOException ignore) {
            System.out.println(ignore.getMessage());
        }
    }

    public static void openAdminDashboard() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(Login.class.getResource("admin_dashboard.fxml")));
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Admin Dashboard");
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();
            primaryStage.hide();
            dashboardStage.setOnCloseRequest(windowEvent -> {
                primaryStage.show();
            });
        } catch (IOException ignore) {
        }
    }
}
