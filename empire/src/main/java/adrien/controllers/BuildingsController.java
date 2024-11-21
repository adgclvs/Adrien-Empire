package adrien.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BuildingsController {

    @FXML
    private HBox listBuildings;

    @FXML
    private VBox buttonsContainer;

    private ButtonController buttonController;

    public void initialize() {
        loadButtonController(); // Charger ButtonController avant de l'initialiser
    if (buttonController != null) {
        buttonController.initialize();
    }
    loadBuildingImages(); // Charger les bâtiments après avoir configuré ButtonController
}
    

    private void loadBuildingImages() {
        List<String> buildingImages = new ArrayList<>();
        buildingImages.add("/adrien/images/buildings/apartmentbuilding.png");
        buildingImages.add("/adrien/images/buildings/cementplant.png");
        buildingImages.add("/adrien/images/buildings/farm.png");
        buildingImages.add("/adrien/images/buildings/house.png");
        buildingImages.add("/adrien/images/buildings/lumbermill.png");
        buildingImages.add("/adrien/images/buildings/quarry.png");
        buildingImages.add("/adrien/images/buildings/steelmill.png");
        buildingImages.add("/adrien/images/buildings/toolfactory.png");
        buildingImages.add("/adrien/images/buildings/woodencabin.png");

        for (String imagePath : buildingImages) {
            Image buildingImage = new Image(getClass().getResourceAsStream(imagePath));
            ImageView buildingImageView = new ImageView(buildingImage);
            buildingImageView.setFitWidth(100);  // Ajustez la largeur
            buildingImageView.setFitHeight(100); // Ajustez la hauteur
            buildingImageView.setPreserveRatio(true); 

            // Ajouter une marge au bâtiment pour laisser de l'espace au bouton menu
            HBox.setMargin(buildingImageView, new Insets(0, 10, 0, 10));

            // Ajouter un gestionnaire d'événements de clic
            buildingImageView.setOnMouseClicked(event -> {
                System.out.println("Building clicked: " + imagePath);
                if (buttonController != null) {
                    buttonController.showButtons(); // Afficher les boutons
                } else {
                    System.out.println("buttonController is null");
                }
            });

            listBuildings.getChildren().add(buildingImageView);
        }
    }

    private void loadButtonController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adrien/views/ButtonsView.fxml"));
            VBox buttons = loader.load();
            buttonController = loader.getController();
            if (buttonController == null) {
                System.out.println("buttonController is null after loading FXML");
            } else {
                System.out.println("buttonController loaded successfully");
            }
            buttonsContainer.getChildren().add(buttons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}