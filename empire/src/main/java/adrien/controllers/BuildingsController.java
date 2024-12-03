package adrien.controllers;

import adrien.MapManager;
import adrien.Observer;
import adrien.SharedState;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BuildingsController {

    @FXML
    private HBox listBuildings;

    @FXML
    private VBox buttonsContainer;

    private BuildingType selectedBuildingType;

    public void initialize() {
        loadBuildingImages();
    }

    public BuildingType getSelectedBuildingType() {
        return selectedBuildingType;
    }

    public void clearSelectedBuildingType() {
        this.selectedBuildingType = null;
    }

    private void loadBuildingImages() {
        listBuildings.getChildren().clear();
        for (BuildingType buildingType : BuildingType.values()) {
            String imagePath = "/adrien/images/buildings/" + buildingType.toString().toLowerCase() + ".png";
            Image buildingImage = new Image(getClass().getResourceAsStream(imagePath));
            ImageView buildingImageView = new ImageView(buildingImage);
            buildingImageView.setFitWidth(100);
            buildingImageView.setFitHeight(100);
            buildingImageView.setPreserveRatio(true);

            // Créer un Tooltip pour afficher le texte lors du survol
            Tooltip tooltip = new Tooltip(buildingType.toString());
            Tooltip.install(buildingImageView, tooltip);

            buildingImageView.setOnMouseClicked(event -> {
                SharedState.setSelectedBuildingType(buildingType);  // Mise à jour du SharedState avec le type sélectionné
                System.out.println("Selected building: " + buildingType);
            });

            listBuildings.getChildren().add(buildingImageView);
        }
    }

    public String getBuildingInfo(Building building) {
        return "Type: " + building.getType() +
               "\nWorkers: " + building.getCurrentWorkers() +
               "\nOperational: " + building.isOperational();
    }

    // private void loadButtonController() {
    //     try {
    //         FXMLLoader loader = new FXMLLoader(getClass().getResource("/adrien/views/ButtonsView.fxml"));
    //         VBox buttons = loader.load();
    //         buttonController = loader.getController();
    //         if (buttonController == null) {
    //             System.out.println("buttonController is null after loading FXML");
    //         } else {
    //             System.out.println("buttonController loaded successfully");
    //         }
    //         buttonsContainer.getChildren().add(buttons);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

}