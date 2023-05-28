package com.example.hellofx;

import com.example.hellofx.Login;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private AnchorPane anchorMainContainer;
    @FXML
    private VBox subContainer;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField addressField;

    @FXML
    public void switchToRegister(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, Login.INIT_WIDTH + 100, Login.INIT_HEIGHT + 100);
        stage.setScene(scene);
        stage.setMinWidth(Login.INIT_WIDTH);
        stage.setMinHeight(Login.INIT_HEIGHT);
        stage.setResizable(true);
        stage.show();
    }

    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, Login.INIT_WIDTH + 100, Login.INIT_HEIGHT + 100);
        stage.setScene(scene);
        stage.setMinWidth(Login.INIT_WIDTH);
        stage.setMinHeight(Login.INIT_HEIGHT);
        stage.setResizable(true);
        stage.show();
    }

    public void clearInput() {
        fullNameField.setText("");
        emailTextField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        phoneNumberField.setText("");
        addressField.setText("");
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordField.getText();

        System.out.println(email);
        System.out.println(password);
        // TODO: Implement login validation and open appropriate window

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        if (email.length() > 0 && password.length() > 0) {
            alert.setContentText("Login Successful!");
            clearInput();
        } else {
            alert.setContentText("Invalid username and password!");
        }
        alert.showAndWait();
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String fullName = fullNameField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String confimPassword = confirmPasswordField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        String sql = """
                INSERT INTO users(user_id, full_name, email, password, phone_number, address) VALUES(DEFAULT, ?, ?, ?, ?, ?)
                """;
        try {
            PreparedStatement preparedStatement = DBController.connection.prepareStatement(sql);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, address);
            int userCreated = preparedStatement.executeUpdate();
            if (userCreated == 1) {
                alert.setContentText("Login Successful!");
                clearInput();
                switchToLogin(event);
                alert.showAndWait();
            } else {
                alert.setContentText("Something went wrong!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            alert.setContentText("Something went wrong!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::centerChildPane);

        anchorMainContainer.widthProperty().addListener((observable, oldValue, newValue) -> {
            centerChildPane();
        });

        anchorMainContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            centerChildPane();
        });
    }

    private void centerChildPane() {
        double paneWidth = anchorMainContainer.getWidth();
        double paneHeight = anchorMainContainer.getHeight();
        double childWidth = subContainer.getWidth();
        double childHeight = subContainer.getHeight();

        double offsetX = (paneWidth - childWidth) / 2;
        double offsetY = (paneHeight - childHeight) / 2;

        AnchorPane.setLeftAnchor(subContainer, offsetX);
        AnchorPane.setTopAnchor(subContainer, offsetY);
    }
}
