package adrien.controllers;

import adrien.observers.Observer;
import adrien.resources.Resource;
import adrien.resources.ResourceType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class ResourcesController implements Observer {

    private static final double IMAGE_WIDTH = 70;
    private static final double IMAGE_HEIGHT = 70;

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
        Resource.getInstance().addObserver(this);
    }

    @Override
    public void update() {
        Platform.runLater(this::updateResourceLabels);
    }

    public void updateResourceImages() {
        Resource resource = Resource.getInstance();

        foodImage.setImage(resource.getResourceImage(ResourceType.FOOD));
        foodImage.setFitWidth(IMAGE_WIDTH);
        foodImage.setFitHeight(IMAGE_HEIGHT);

        woodImage.setImage(resource.getResourceImage(ResourceType.WOOD));
        woodImage.setFitWidth(IMAGE_WIDTH);
        woodImage.setFitHeight(IMAGE_HEIGHT);

        stoneImage.setImage(resource.getResourceImage(ResourceType.STONE));
        stoneImage.setFitWidth(IMAGE_WIDTH);
        stoneImage.setFitHeight(IMAGE_HEIGHT);

        coalImage.setImage(resource.getResourceImage(ResourceType.COAL));
        coalImage.setFitWidth(IMAGE_WIDTH);
        coalImage.setFitHeight(IMAGE_HEIGHT);

        ironImage.setImage(resource.getResourceImage(ResourceType.IRON));
        ironImage.setFitWidth(IMAGE_WIDTH);
        ironImage.setFitHeight(IMAGE_HEIGHT);

        steelImage.setImage(resource.getResourceImage(ResourceType.STEEL));
        steelImage.setFitWidth(IMAGE_WIDTH);
        steelImage.setFitHeight(IMAGE_HEIGHT);

        cementImage.setImage(resource.getResourceImage(ResourceType.CEMENT));
        cementImage.setFitWidth(IMAGE_WIDTH);
        cementImage.setFitHeight(IMAGE_HEIGHT);

        lumberImage.setImage(resource.getResourceImage(ResourceType.LUMBER));
        lumberImage.setFitWidth(IMAGE_WIDTH);
        lumberImage.setFitHeight(IMAGE_HEIGHT);

        toolsImage.setImage(resource.getResourceImage(ResourceType.TOOLS));
        toolsImage.setFitWidth(IMAGE_WIDTH);
        toolsImage.setFitHeight(IMAGE_HEIGHT);
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