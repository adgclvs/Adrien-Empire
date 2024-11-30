package adrien.controllers;

import java.io.IOException;

import adrien.GameManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController {
    private int gridHeight;
    private int gridWidth;
    private GameManager gameManager;

    @FXML
    private BorderPane mainPane;

    @FXML
    private MapController mapController;

    @FXML
    private ResourcesController resourcesController;

    @FXML
    private BuildingsController buildingsController;

    @FXML
    public void initialize() {
        try {
            // Charger les vues et les contrôleurs
            Pane buildingsView = loadView("/adrien/views/BuildingsView.fxml");
            Pane mapView = loadView("/adrien/views/MapView.fxml");
            Pane resourcesView = loadView("/adrien/views/ResourcesView.fxml");

            // Initialiser les dimensions de la grille et le GameManager
            gridHeight = 20;
            gridWidth = 40;
            gameManager = new GameManager(gridWidth, gridHeight);
            gameManager.startGame();

            // Passer le GameManager et BuildingsController au MapController
            if (mapController != null) {
                mapController.setGameManager(gameManager);
                mapController.setBuildingsController(buildingsController);
                mapController.initialize();
            } else {
                System.out.println("MapController is not set.");
            }

            // Ajouter les vues au BorderPane principal
            mainPane.setLeft(mapView);
            mainPane.setBottom(buildingsView);
            mainPane.setRight(resourcesView);

            // Initialiser les autres contrôleurs
            if (resourcesController != null) {
                resourcesController.setGameManager(gameManager);
                resourcesController.initialize();
            }
            if (buildingsController != null) {
                buildingsController.initialize();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        // Ajouter un gestionnaire d'événements pour l'événement de fermeture de la fenêtre
        primaryStage.setOnCloseRequest(event -> {
            gameManager.stopGame();
        });
    }

    private Pane loadView(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Pane pane = loader.load();

        // Injecter le contrôleur si nécessaire
        if (fxmlPath.contains("BuildingsView")) {
            buildingsController = loader.getController();
        } else if (fxmlPath.contains("MapView")) {
            mapController = loader.getController();
        } else if (fxmlPath.contains("ResourcesView")) {
            resourcesController = loader.getController();
        }

        return pane;
    }
}