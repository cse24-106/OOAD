package com.felix.bankingsystem.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
            // Load the login screen
            Parent root = FXMLLoader.load(getClass().getResource("/com/felix/bankingsystem/FXML files/LoginView.fxml"));
            primaryStage.setTitle("Capital Bank - Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
