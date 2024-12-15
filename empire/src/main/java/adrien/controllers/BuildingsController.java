package adrien.controllers;

import adrien.buildings.BuildingsManager.*;
import adrien.exceptions.BuildingException;
import adrien.game.ImageCache;
import adrien.game.SharedState;
import adrien.observers.Observer;
import adrien.resources.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.util.HashMap;
import java.util.Map;

public class BuildingsController implements Observer {

    private static final double BUILDINGS_IMAGE_WIDTH = 100;
    private static final double BUILDINGS_IMAGE_HEIGHT = 100;
    private static final double RESOURCE_IMAGE_SIZE = 50;
    private static final double GAP = 10;

    @FXML
    private HBox listBuildings;

    private final Map<BuildingType, Popup> popups = new HashMap<>();
    private final Map<String, Label> resourceLabels = new HashMap<>();
    private static ImageView selectedBuildingImageView;

    @FXML
    public void initialize() {
        Resource.getInstance().addObserver(this);
        loadBuildingImages();
    }

    @Override
    public void update() {
        Platform.runLater(this::updateResourceLabels);
    }

    /**
     * Load the images of the buildings
     * and create the popups
     */
    private void loadBuildingImages() {
        listBuildings.getChildren().clear();
        popups.clear();

        for (BuildingType buildingType : BuildingType.values()) {
            ImageView buildingImageView = createBuildingImageView(buildingType);
            Popup popup = createResourcePopup(buildingType);

            buildingImageView.setOnMouseEntered(event -> popup.show(buildingImageView, event.getScreenX() + GAP, event.getScreenY() + GAP));
            buildingImageView.setOnMouseExited(event -> popup.hide());
            buildingImageView.setOnMouseClicked(event -> selectBuilding(buildingType, buildingImageView));

            listBuildings.getChildren().add(buildingImageView);
            popups.put(buildingType, popup);
        }
    }

    /**
     * Create an ImageView for a building
     * @param buildingType the type of the building
     * @return the ImageView
     */
    private ImageView createBuildingImageView(BuildingType buildingType) {
        String imagePath = "/adrien/images/buildings/!" + buildingType.toString().toLowerCase() + ".png";
        Image buildingImage = ImageCache.getImage(imagePath);
        ImageView buildingImageView = new ImageView(buildingImage);
        buildingImageView.setFitWidth(BUILDINGS_IMAGE_WIDTH);
        buildingImageView.setFitHeight(BUILDINGS_IMAGE_HEIGHT);
        buildingImageView.setPreserveRatio(true);
        return buildingImageView;
    }

    /**
     * Select a building
     * @param buildingType the type of the building
     */
    public static void selectBuilding(BuildingType buildingType, ImageView buildingImageView) {
        // Réinitialiser l'effet visuel de l'ancien bâtiment sélectionné
        if (selectedBuildingImageView != null) {
            selectedBuildingImageView.setEffect(null);
        }
    
        // Si aucun bâtiment n'est sélectionné, réinitialiser et retourner
        if (buildingType == null) {
            selectedBuildingImageView = null;
            SharedState.setSelectedBuildingType(null);
            return;
        }
    
        // Appliquer un effet visuel au bâtiment sélectionné
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.RED);
        borderGlow.setWidth(20);
        borderGlow.setHeight(20);
        buildingImageView.setEffect(borderGlow);
    
        // Mettre à jour la référence du bâtiment sélectionné
        selectedBuildingImageView = buildingImageView;
    
        // Mettre à jour l'état partagé
        SharedState.setSelectedBuildingType(buildingType);
    }

    /**
     * Create a popup for a building
     * @param buildingType the type of the building
     * @return the popup
     */
    private Popup createResourcePopup(BuildingType buildingType) {
        Popup popup = new Popup();
        VBox popupContent = new VBox();
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        popup.getContent().add(popupContent);

        Building prototype = null;
        try{
            prototype = BuildingPrototypes.getPrototype(buildingType);
        }catch (BuildingException e){
            e.printStackTrace();
        }

        for (ResourceRequirement requirement : prototype.getConstructionMaterials()) {
            HBox resourceBox = createResourceBox(requirement);
            popupContent.getChildren().add(resourceBox);
        }

        return popup;
    }

    /**
     * Create a box for a resource requirement
     * @param requirement the resource requirement
     * @return the box
     */
    private HBox createResourceBox(ResourceRequirement requirement) {
        String resourceImagePath = "/adrien/images/resources/" + requirement.getResourceType().toString().toLowerCase() + ".png";
        ImageView resourceImageView = createImageView(resourceImagePath, RESOURCE_IMAGE_SIZE);

        int requiredAmount = requirement.getQuantity();
        int availableAmount = Resource.getInstance().getResource(requirement.getResourceType());

        Label resourceLabel = new Label(availableAmount + "/" + requiredAmount);
        resourceLabel.setId(requirement.getResourceType().toString());
        resourceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        resourceLabel.setTextFill(availableAmount >= requiredAmount ? Color.GREEN : Color.RED);

        // Ajouter le label dans une map pour un accès rapide lors de la mise à jour
        resourceLabels.put(requirement.getResourceType().toString(), resourceLabel);

        HBox resourceBox = new HBox(resourceImageView, resourceLabel);
        resourceBox.setAlignment(Pos.CENTER);
        return resourceBox;
    }

    /**
     * Create an ImageView for a resource
     * @param imagePath the path of the image
     * @param size the size of the image
     * @return the ImageView
     */
    private ImageView createImageView(String imagePath, double size) {
        Image resourceImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(resourceImage);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Update the labels of the resources
     */
    private void updateResourceLabels() {
        for (Map.Entry<BuildingType, Popup> entry : popups.entrySet()) {
            BuildingType buildingType = entry.getKey();
            Popup popup = entry.getValue();
            VBox popupContent = (VBox) popup.getContent().get(0);

            // Récupérer le prototype du bâtiment
            Building prototype = null;
            try {
                prototype = BuildingPrototypes.getPrototype(buildingType);
            }catch (BuildingException e){
                e.printStackTrace();
            }

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
