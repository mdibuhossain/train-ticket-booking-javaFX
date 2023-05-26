package com.example.hellofx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane anchorMainContainer;
    @FXML
    private VBox subContainer;
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
        if (email.length() > 0 && password.length() > 0) alert.setContentText("Login Successful!");
        else alert.setContentText("Invalid username and password!");
        alert.showAndWait();
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
