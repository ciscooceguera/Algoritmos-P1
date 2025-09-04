package com.example.practica_1_algoritmos;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import solitaire.SolitaireGame;

import javax.swing.*;
import javax.swing.text.html.ImageView;

public class Controller {
    @FXML
    private SolitaireGame game;
    private StackPane tableauPane;


    @FXML
    protected void onHelloButtonClick() {
       // welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void initialize() throws InterruptedException {
        game = new SolitaireGame();
       // renderBoard();

    }

    public void renderBoard() {

    }



}