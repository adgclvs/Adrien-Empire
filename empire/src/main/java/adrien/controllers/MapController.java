package adrien.controllers;

import adrien.GameManager;
import adrien.MapManager;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

public class MapController {

    @FXML
    private GridPane mapPane;

    private GameManager gameManager;
    private int gridHeight;
    private int gridWidth;

    public void initialize() {
        gridHeight = 20;
        gridWidth = 40;
        MapManager.getInstance(gridWidth, gridHeight);
        displayMap();
    }
    

    private void displayMap() {
        // Obtenez la taille de l'écran
        double screenWidth = Screen.getPrimary().getBounds().getWidth();

        // Calculez la taille des cases de la grille
        double cellsize = (screenWidth - 100) / gridWidth;


        Image grassImage = new Image(getClass().getResourceAsStream("/adrien/images/Grass.png"));
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                ImageView imageView = new ImageView(grassImage);
                imageView.setFitWidth(cellsize);
                imageView.setFitHeight(cellsize);
                mapPane.add(imageView, j, i);
            }
        }
    }

    // Méthodes pour gérer les interactions avec la carte
}