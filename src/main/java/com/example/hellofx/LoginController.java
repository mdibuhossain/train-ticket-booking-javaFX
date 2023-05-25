package com.example.hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordField.getText();

        System.out.println(email);
        System.out.println(password);
        // TODO: Implement login validation and open appropriate window

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        if (email.length() > 0 && password.length() > 0)
            alert.setContentText("Login Successful!");
        else
            alert.setContentText("Invalid username and password!");
        alert.showAndWait();
    }

}
