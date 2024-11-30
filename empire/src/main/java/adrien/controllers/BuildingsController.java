package adrien.controllers;

import adrien.MapManager;
import adrien.Observer;
import adrien.SharedState;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import javafx.fxml.FXML;
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

    private BuildingType selectedBuildingType;

    public void initialize() {
        MapManager.getInstance().addObserver(this);
        loadBuildingImages();
    }

    @Override
    public void update() {
        // updateBuildingInfo();
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
    
            buildingImageView.setOnMouseClicked(event -> {
                SharedState.setSelectedBuildingType(buildingType);  // Mise à jour du SharedState avec le type sélectionné
                System.out.println("Selected building: " + buildingType);
            });
    
            VBox buildingBox = new VBox(buildingImageView, new Label(buildingType.toString()));
            listBuildings.getChildren().add(buildingBox);
        }
    }
    
    // private void updateBuildingInfo() {
    //     Building[] allBuildings = MapManager.getAllBuildings();
    //     for (int i = 0; i < listBuildings.getChildren().size(); i++) {
    //         VBox buildingBox = (VBox) listBuildings.getChildren().get(i);
    //         if (i < allBuildings.length) {
    //             Building building = allBuildings[i];
    //             Label buildingInfo = (Label) buildingBox.getChildren().get(1);
    //             buildingInfo.setText(getBuildingInfo(building));
    //         } else {
    //             // Si le nombre de bâtiments est inférieur au nombre de vues, effacez les informations restantes
    //             Label buildingInfo = (Label) buildingBox.getChildren().get(1);
    //             buildingInfo.setText("");
    //         }
    //     }
    // }

    // private void updateBuildingInfo() {
    //     for (int i = 0; i < listBuildings.getChildren().size(); i++) {
    //         VBox buildingBox = (VBox) listBuildings.getChildren().get(i);
    //         Building building = MapManager.getAllBuildings()[i];
    //         Label buildingInfo = (Label) buildingBox.getChildren().get(1);
    //         buildingInfo.setText(getBuildingInfo(building));
    //     }
    // }

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