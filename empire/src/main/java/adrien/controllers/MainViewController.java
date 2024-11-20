package adrien.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainViewController {
    @FXML
    private BorderPane mainPane;

    private ButtonsController buttonsController;
    private MapController mapController;
    private ResourcesController resourcesController;

    @FXML
    public void initialize() {
        try {
            // Charger et lier ButtonsView
            FXMLLoader buttonsLoader = new FXMLLoader(getClass().getResource("/adrien/views/ButtonsView.fxml"));
            AnchorPane buttonsView = buttonsLoader.load();
            buttonsController = buttonsLoader.getController();
            mainPane.setTop(buttonsView);

            // Charger et lier MapView
            FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("/adrien/views/MapView.fxml"));
            AnchorPane mapView = mapLoader.load();
            mapController = mapLoader.getController();
            mapController.setMainViewController(this); // Si nécessaire
            mapController.initializeMap(20, 60); // Initialiser la carte avec les dimensions souhaitées
            mainPane.setCenter(mapView);

            // Charger et lier ResourcesView
            FXMLLoader resourcesLoader = new FXMLLoader(getClass().getResource("/adrien/views/ResourcesView.fxml"));
            AnchorPane resourcesView = resourcesLoader.load();
            resourcesController = resourcesLoader.getController();
            mainPane.setLeft(resourcesView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
