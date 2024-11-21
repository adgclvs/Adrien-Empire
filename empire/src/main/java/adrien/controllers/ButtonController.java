package adrien.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ButtonController {

    @FXML
    private VBox buttonsContainer;

    public void initialize() {
        // Initialisation si nécessaire
    }

    public void showButtons() {
        System.out.println("showButtons called");
        buttonsContainer.getChildren().clear(); // Efface les boutons existants

        ImageView button1 = createImageButton("/adrien/images/buttons/menu.png");
        ImageView button2 = createImageButton("/adrien/images/buttons/button2.png");
        ImageView button3 = createImageButton("/adrien/images/buttons/button3.png");

        buttonsContainer.getChildren().addAll(button1, button2, button3);
    }

    private ImageView createImageButton(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);  // Ajustez la largeur
        imageView.setFitHeight(50); // Ajustez la hauteur
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(event -> handleButtonClick(imagePath));
        return imageView;
    }

    private void handleButtonClick(String imagePath) {
        // Gérer le clic sur le bouton image
        System.out.println("Button clicked: " + imagePath);
    }
}