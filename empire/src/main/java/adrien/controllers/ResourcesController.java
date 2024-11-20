package adrien.controllers;

import adrien.resources.Resource;
import adrien.resources.ResourceType;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class ResourcesController {

    @FXML
    private ImageView foodImage;

    @FXML
    private ImageView woodImage;

    @FXML
    private ImageView stoneImage;

    @FXML
    private ImageView coalImage;

    @FXML
    private ImageView ironImage;

    @FXML
    private ImageView steelImage;

    @FXML
    private ImageView cementImage;

    @FXML
    private ImageView lumberImage;

    @FXML
    private ImageView toolsImage;

    @FXML
    private Label foodLabel;

    @FXML
    private Label woodLabel;

    @FXML
    private Label stoneLabel;

    @FXML
    private Label coalLabel;

    @FXML
    private Label ironLabel;

    @FXML
    private Label steelLabel;

    @FXML
    private Label cementLabel;

    @FXML
    private Label lumberLabel;

    @FXML
    private Label toolsLabel;

    @FXML
    public void initialize() {
        Resource.getInstance();
        updateResourceImages();
        updateResourceLabels();
    }

    public void updateResourceImages() {
        double imageWidth = 70; // Largeur des images
        double imageHeight = 70; // Hauteur des images

        foodImage.setImage(new Image(getClass().getResourceAsStream("/adrien/images/resources/food.png")));
        foodImage.setFitWidth(imageWidth);
        foodImage.setFitHeight(imageHeight);

        woodImage.setImage(new Image(getClass().getResourceAsStream("/adrien/images/resources/wood.png")));
        woodImage.setFitWidth(imageWidth);
        woodImage.setFitHeight(imageHeight);

        stoneImage.setImage(new Image(getClass().getResourceAsStream("/adrien/images/resources/stone.png")));
        stoneImage.setFitWidth(imageWidth);
        stoneImage.setFitHeight(imageHeight);

        coalImage.setImage(new Image(getClass().getResourceAsStream("/adrien/images/resources/coal.png")));
        coalImage.setFitWidth(imageWidth);
        coalImage.setFitHeight(imageHeight);

        ironImage.setImage(new Image(getClass().getResourceAsStream("/adrien/images/resources/iron.png")));
        ironImage.setFitWidth(imageWidth);
        ironImage.setFitHeight(imageHeight);

        steelImage.setImage(new Image(getClass().getResourceAsStream("/adrien/images/resources/steel.png")));
        steelImage.setFitWidth(imageWidth);
        steelImage.setFitHeight(imageHeight);

        cementImage.setImage(new Image(getClass().getResourceAsStream("/adrien/images/resources/cement.png")));
        cementImage.setFitWidth(imageWidth);
        cementImage.setFitHeight(imageHeight);

        lumberImage.setImage(new Image(getClass().getResourceAsStream("/adrien/images/resources/lumber.png")));
        lumberImage.setFitWidth(imageWidth);
        lumberImage.setFitHeight(imageHeight);

        toolsImage.setImage(new Image(getClass().getResourceAsStream("/adrien/images/resources/tools.png")));
        toolsImage.setFitWidth(imageWidth);
        toolsImage.setFitHeight(imageHeight);
    }

    public void updateResourceLabels() {
        foodLabel.setText("" + Resource.getResource(ResourceType.FOOD));
        woodLabel.setText("" + Resource.getResource(ResourceType.WOOD));
        stoneLabel.setText("" + Resource.getResource(ResourceType.STONE));
        coalLabel.setText("" + Resource.getResource(ResourceType.COAL));
        ironLabel.setText("" + Resource.getResource(ResourceType.IRON));
        steelLabel.setText("" + Resource.getResource(ResourceType.STEEL));
        cementLabel.setText("" + Resource.getResource(ResourceType.CEMENT));
        lumberLabel.setText("" + Resource.getResource(ResourceType.LUMBER));
        toolsLabel.setText("" + Resource.getResource(ResourceType.TOOLS));
    }

    // Méthodes pour mettre à jour les ressources
}