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
            gridHeight = 35;
            gridWidth = 35;
            gameManager = new GameManager(gridWidth, gridHeight);
            gameManager.startGame();

            // Initialiser les contrôleurs
            if (mapController != null) {
                mapController.setGameManager(gameManager);
                mapController.setBuildingsController(buildingsController);
                mapController.setResourcesController(resourcesController);
                mapController.initialize();
            } 
            if (resourcesController != null) {
                resourcesController.initialize();
            }
            if (buildingsController != null) {
                buildingsController.initialize();
            }

            // Ajouter les vues au BorderPane principal
            mainPane.setLeft(mapView);
            mainPane.setBottom(buildingsView);
            mainPane.setRight(resourcesView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop le timer du jeu lorsque la fenêtre se ferme
     * @param primaryStage
     */
    public void setPrimaryStage(Stage primaryStage) {
        // Ajouter un gestionnaire d'événements pour l'événement de fermeture de la fenêtre
        primaryStage.setOnCloseRequest(event -> {
            gameManager.stopGame();
        });
    }

    /**
     * Charger une vue FXML et renvoyer le panneau
     * @param fxmlPath
     * @return
     * @throws IOException
     */
    private Pane loadView(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Pane pane = loader.load();

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