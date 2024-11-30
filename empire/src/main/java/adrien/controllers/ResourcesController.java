package adrien.controllers;

import adrien.GameManager;
import adrien.Observer;
import adrien.resources.Resource;
import adrien.resources.ResourceType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class ResourcesController implements Observer {

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

    // private GameManager gameManager;

    // public void setGameManager(GameManager gameManager) {
    //     this.gameManager = gameManager;
    //     // gameManager.addObserver(this); // Ajouter ce contrôleur comme observateur
    // }

    @FXML
    public void initialize() {
        Resource.getInstance().addObserver(this);
        updateResourceImages();
        updateResourceLabels();
    }

    @Override
    public void update() {
        Platform.runLater(() -> {
            updateResourceLabels();
        });
    }

    public void updateResourceImages() {
        double imageWidth = 70;
        double imageHeight = 70;

        foodImage.setImage(Resource.getResourceImage(ResourceType.FOOD));
        foodImage.setFitWidth(imageWidth);
        foodImage.setFitHeight(imageHeight);

        woodImage.setImage(Resource.getResourceImage(ResourceType.WOOD));
        woodImage.setFitWidth(imageWidth);
        woodImage.setFitHeight(imageHeight);

        stoneImage.setImage(Resource.getResourceImage(ResourceType.STONE));
        stoneImage.setFitWidth(imageWidth);
        stoneImage.setFitHeight(imageHeight);

        coalImage.setImage(Resource.getResourceImage(ResourceType.COAL));
        coalImage.setFitWidth(imageWidth);
        coalImage.setFitHeight(imageHeight);

        ironImage.setImage(Resource.getResourceImage(ResourceType.IRON));
        ironImage.setFitWidth(imageWidth);
        ironImage.setFitHeight(imageHeight);

        steelImage.setImage(Resource.getResourceImage(ResourceType.STEEL));
        steelImage.setFitWidth(imageWidth);
        steelImage.setFitHeight(imageHeight);

        cementImage.setImage(Resource.getResourceImage(ResourceType.CEMENT));
        cementImage.setFitWidth(imageWidth);
        cementImage.setFitHeight(imageHeight);

        lumberImage.setImage(Resource.getResourceImage(ResourceType.LUMBER));
        lumberImage.setFitWidth(imageWidth);
        lumberImage.setFitHeight(imageHeight);

        toolsImage.setImage(Resource.getResourceImage(ResourceType.TOOLS));
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

}