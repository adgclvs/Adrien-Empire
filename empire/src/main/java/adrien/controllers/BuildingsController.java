package adrien.controllers;

import adrien.MapManager;
import adrien.Observer;
import adrien.buildings.BuildingsManager.Building;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BuildingsController implements Observer {

    @FXML
    private HBox listBuildings;

    @FXML
    private VBox buttonsContainer;

    public void initialize() {
        MapManager.getInstance(10, 10).addObserver(this);
        loadBuildingImages();
    }

    @Override
    public void update() {
        updateBuildingInfo();
    }

    private void loadBuildingImages() {
        listBuildings.getChildren().clear();
        for (Building building : MapManager.getAllBuildings()) {
            String imagePath = "/adrien/images/buildings/" + building.getType().toString().toLowerCase() + ".png";
            Image buildingImage = new Image(getClass().getResourceAsStream(imagePath));
            ImageView buildingImageView = new ImageView(buildingImage);
            buildingImageView.setFitWidth(100);  // Ajustez la largeur
            buildingImageView.setFitHeight(100); // Ajustez la hauteur
            buildingImageView.setPreserveRatio(true);

            // Ajouter une marge au bâtiment pour laisser de l'espace au bouton menu
            HBox.setMargin(buildingImageView, new Insets(0, 10, 0, 10));

            // Ajouter un gestionnaire d'événements de clic
            buildingImageView.setOnMouseClicked(event -> {
                System.out.println("Building clicked: " + building.getType());
                // Vous pouvez ajouter plus de logique ici pour gérer le clic sur le bâtiment
            });

            VBox buildingBox = new VBox();
            buildingBox.getChildren().add(buildingImageView);

            Label buildingInfo = new Label(getBuildingInfo(building));
            buildingBox.getChildren().add(buildingInfo);

            listBuildings.getChildren().add(buildingBox);
        }
    }

    private void updateBuildingInfo() {
        for (int i = 0; i < listBuildings.getChildren().size(); i++) {
            VBox buildingBox = (VBox) listBuildings.getChildren().get(i);
            Building building = MapManager.getAllBuildings()[i];
            Label buildingInfo = (Label) buildingBox.getChildren().get(1);
            buildingInfo.setText(getBuildingInfo(building));
        }
    }

    private String getBuildingInfo(Building building) {
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