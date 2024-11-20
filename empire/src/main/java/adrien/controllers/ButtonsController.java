package adrien.controllers;

import java.util.ArrayList;
import java.util.List;

import adrien.GameManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ButtonsController {

    @FXML
    private ImageView menuImage;

    @FXML
    private HBox customMenu; // HBox pour disposer les images en ligne

    private GameManager gameManager;

    public void initialize() {

        // Définir l'image du menu
        String imagePath = "/adrien/images/menu.png";
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        if (image.isError()) {
            System.err.println("Error loading image: " + imagePath);
        } else {
            menuImage.setImage(image);
            menuImage.setFitWidth(100); // Ajustez la largeur de l'image
            menuImage.setFitHeight(100); // Ajustez la hauteur de l'image
        }

        loadBuildingImages();
    }

    @FXML
    private void toggleMenu() {
        // Basculer l'affichage du menu (visible/invisible)
        customMenu.setVisible(!customMenu.isVisible());
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

            customMenu.getChildren().add(buildingImageView);
        }
    }
}
