package adrien.controllers;

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
        Resource resource = Resource.getInstance();

        foodImage.setImage(resource.getResourceImage(ResourceType.FOOD));
        foodImage.setFitWidth(imageWidth);
        foodImage.setFitHeight(imageHeight);

        woodImage.setImage(resource.getResourceImage(ResourceType.WOOD));
        woodImage.setFitWidth(imageWidth);
        woodImage.setFitHeight(imageHeight);

        stoneImage.setImage(resource.getResourceImage(ResourceType.STONE));
        stoneImage.setFitWidth(imageWidth);
        stoneImage.setFitHeight(imageHeight);

        coalImage.setImage(resource.getResourceImage(ResourceType.COAL));
        coalImage.setFitWidth(imageWidth);
        coalImage.setFitHeight(imageHeight);

        ironImage.setImage(resource.getResourceImage(ResourceType.IRON));
        ironImage.setFitWidth(imageWidth);
        ironImage.setFitHeight(imageHeight);

        steelImage.setImage(resource.getResourceImage(ResourceType.STEEL));
        steelImage.setFitWidth(imageWidth);
        steelImage.setFitHeight(imageHeight);

        cementImage.setImage(resource.getResourceImage(ResourceType.CEMENT));
        cementImage.setFitWidth(imageWidth);
        cementImage.setFitHeight(imageHeight);

        lumberImage.setImage(resource.getResourceImage(ResourceType.LUMBER));
        lumberImage.setFitWidth(imageWidth);
        lumberImage.setFitHeight(imageHeight);

        toolsImage.setImage(resource.getResourceImage(ResourceType.TOOLS));
        toolsImage.setFitWidth(imageWidth);
        toolsImage.setFitHeight(imageHeight);
    }

    public void updateResourceLabels() {
        Resource resource = Resource.getInstance();
        foodLabel.setText("" + resource.getResource(ResourceType.FOOD));
        woodLabel.setText("" + resource.getResource(ResourceType.WOOD));
        stoneLabel.setText("" + resource.getResource(ResourceType.STONE));
        coalLabel.setText("" + resource.getResource(ResourceType.COAL));
        ironLabel.setText("" + resource.getResource(ResourceType.IRON));
        steelLabel.setText("" + resource.getResource(ResourceType.STEEL));
        cementLabel.setText("" + resource.getResource(ResourceType.CEMENT));
        lumberLabel.setText("" + resource.getResource(ResourceType.LUMBER));
        toolsLabel.setText("" + resource.getResource(ResourceType.TOOLS));
    }

}