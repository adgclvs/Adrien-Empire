package adrien.controllers;

import adrien.MapManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class MapController {

    @FXML
    private GridPane mapPane;
    private int gridHeight;
    private int gridWidth;

    public void initialize() {
        gridHeight = 20;
        gridWidth = 40;
        MapManager.getInstance(gridWidth, gridHeight);
        MapManager.displayMap(mapPane);
    }

}