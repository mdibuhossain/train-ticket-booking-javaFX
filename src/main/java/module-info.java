module com.example.hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires java.sql;
    requires java.desktop;


    opens com.example.hellofx to javafx.fxml;
    exports com.example.hellofx;
    exports com.example.hellofx.models;
}