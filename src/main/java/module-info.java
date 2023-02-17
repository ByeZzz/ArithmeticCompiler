module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires org.junit.jupiter.api;


    opens com.example.gui to javafx.fxml;
    //open
    exports com.example.gui;
}