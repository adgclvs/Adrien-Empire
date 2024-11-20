package adrien.controllers;

import adrien.GameManager;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainController {
    private GameManager gameManager;
    private int gridHeight;
    private int gridWidth;

    @FXML
    private BorderPane mainPane;

    @FXML
    private MapController mapController;

    @FXML
    private ResourcesController resourcesController;

    @FXML
    private ButtonsController buttonsController;

    @FXML
    public void initialize() {
        gridHeight = 20;
        gridWidth = 40;
        gameManager = new GameManager(gridWidth, gridHeight);
        // Vérifiez que les contrôleurs enfants ne sont pas null
        if (mapController != null) {
            mapController.initialize();
        }
        if (resourcesController != null) {
            resourcesController.initialize(gameManager);
        }
        if (buttonsController != null) {
            buttonsController.initialize(gameManager);
        }
    }

    // Méthodes pour coordonner les interactions entre les contrôleurs
}