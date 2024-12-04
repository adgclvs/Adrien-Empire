package adrien.controllers;

import adrien.buildings.BuildingsManager.BuildingType;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingPrototypes;
import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;
import adrien.Observer;
import adrien.SharedState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class BuildingsController implements Observer {

    @FXML
    private HBox listBuildings;

    private Map<BuildingType, Popup> popups = new HashMap<>();

    @FXML
    public void initialize() {
        Resource.getInstance().addObserver(this);
        loadBuildingImages();
    }

    @Override
    public void update() {
        // Mettre à jour les labels de ressources dans les popups lorsque les ressources changent
        Platform.runLater(this::updateResourceLabels);
    }

    private void loadBuildingImages() {
        listBuildings.getChildren().clear();
        popups.clear();
        for (BuildingType buildingType : BuildingType.values()) {
            String imagePath = "/adrien/images/buildings/!" + buildingType.toString().toLowerCase() + ".png";
            Image buildingImage = new Image(getClass().getResourceAsStream(imagePath));
            ImageView buildingImageView = new ImageView(buildingImage);
            buildingImageView.setFitWidth(100);
            buildingImageView.setFitHeight(100);
            buildingImageView.setPreserveRatio(true);

            // Créer une Popup pour afficher les ressources nécessaires lors du survol
            Popup popup = createResourcePopup(buildingType);
            popups.put(buildingType, popup);

            buildingImageView.setOnMouseEntered(event -> {
                popup.show(buildingImageView, event.getScreenX() + 10, event.getScreenY() + 10);
            });

            buildingImageView.setOnMouseExited(event -> {
                popup.hide();
            });

            buildingImageView.setOnMouseClicked(event -> {
                SharedState.setSelectedBuildingType(buildingType);  // Mise à jour du SharedState avec le type sélectionné
                System.out.println("Selected building: " + buildingType);
            });

            listBuildings.getChildren().add(buildingImageView);
        }
    }

    private Popup createResourcePopup(BuildingType buildingType) {
        Popup popup = new Popup();
        VBox popupContent = new VBox();
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        popup.getContent().add(popupContent);

        // Récupérer le prototype du bâtiment
        Building prototype = BuildingPrototypes.getPrototype(buildingType);

        // Ajouter les informations sur les ressources nécessaires
        for (ResourceRequirement requirement : prototype.getConstructionMaterials()) {
            int requiredAmount = requirement.getQuantity();
            int availableAmount = Resource.getInstance().getResource(requirement.getResourceType());

            // Charger l'image de la ressource
            String resourceImagePath = "/adrien/images/resources/" + requirement.getResourceType().toString().toLowerCase() + ".png";
            Image resourceImage = new Image(getClass().getResourceAsStream(resourceImagePath));
            ImageView resourceImageView = new ImageView(resourceImage);
            resourceImageView.setFitWidth(50);
            resourceImageView.setFitHeight(50);
            resourceImageView.setPreserveRatio(true);

            // Créer un label pour la quantité
            Label resourceLabel = new Label(availableAmount + "/" + requiredAmount);
            resourceLabel.setId(requirement.getResourceType().toString()); // Utiliser l'ID pour identifier le label
            resourceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            if (availableAmount >= requiredAmount) {
                resourceLabel.setTextFill(Color.GREEN);
            } else {
                resourceLabel.setTextFill(Color.RED);
            }

            HBox resourceBox = new HBox(resourceImageView, resourceLabel);
            resourceBox.setAlignment(Pos.CENTER);
            popupContent.getChildren().add(resourceBox);
        }

        return popup;
    }

    private void updateResourceLabels() {
        for (Map.Entry<BuildingType, Popup> entry : popups.entrySet()) {
            BuildingType buildingType = entry.getKey();
            Popup popup = entry.getValue();
            VBox popupContent = (VBox) popup.getContent().get(0);

            // Récupérer le prototype du bâtiment
            Building prototype = BuildingPrototypes.getPrototype(buildingType);

            // Mettre à jour les informations sur les ressources nécessaires
            for (ResourceRequirement requirement : prototype.getConstructionMaterials()) {
                int requiredAmount = requirement.getQuantity();
                int availableAmount = Resource.getInstance().getResource(requirement.getResourceType());

                // Mettre à jour le label pour la quantité
                for (javafx.scene.Node node : popupContent.getChildren()) {
                    if (node instanceof HBox) {
                        HBox resourceBox = (HBox) node;
                        for (javafx.scene.Node child : resourceBox.getChildren()) {
                            if (child instanceof Label) {
                                Label resourceLabel = (Label) child;
                                if (resourceLabel.getId().equals(requirement.getResourceType().toString())) {
                                    resourceLabel.setText(availableAmount + "/" + requiredAmount);
                                    if (availableAmount >= requiredAmount) {
                                        resourceLabel.setTextFill(Color.GREEN);
                                    } else {
                                        resourceLabel.setTextFill(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}