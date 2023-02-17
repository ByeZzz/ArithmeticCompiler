package com.example.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //初始化设置
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("GuiView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Compile!");
        //窗体自由调整大小
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}