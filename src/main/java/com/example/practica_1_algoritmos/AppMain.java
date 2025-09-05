package com.example.practica_1_algoritmos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppMain extends Application {
    @Override
    public void start(Stage stage) {
        BorderPane mainPane = new BorderPane();
     //   new Controller(mainPane);
        new Controller(mainPane);

        Scene escena = new Scene(mainPane, 800, 600);
        stage.setTitle("Solitario");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) { launch(args); }
}