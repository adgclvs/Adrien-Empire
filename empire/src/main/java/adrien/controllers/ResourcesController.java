package adrien.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import adrien.resources.Resource;
import adrien.resources.ResourceType;

public class ResourcesController {

    @FXML
    private VBox resourcesView;

    @FXML
    private Label woodLabel;
    @FXML
    private Label stoneLabel;
    @FXML
    private Label ironLabel;
    @FXML
    private Label coalLabel;
    @FXML
    private Label steelLabel;
    @FXML
    private Label toolsLabel;
    @FXML
    private Label foodLabel;

    public void updateResourceLabels(int wood, int stone, int iron, int coal, int steel, int tools, int food) {
        woodLabel.setText("Bois: " + wood);
        stoneLabel.setText("Pierre: " + stone);
        ironLabel.setText("Fer: " + iron);
        coalLabel.setText("Charbon: " + coal);
        steelLabel.setText("Acier: " + steel);
        toolsLabel.setText("Outils: " + tools);
        foodLabel.setText("Nourriture: " + food);
    }

    @FXML
    private void handleDisplayResources() {
    updateResourceLabels(
        Resource.getResource(ResourceType.WOOD),
        Resource.getResource(ResourceType.STONE),
        Resource.getResource(ResourceType.IRON),
        Resource.getResource(ResourceType.COAL),
        Resource.getResource(ResourceType.STEEL),
        Resource.getResource(ResourceType.TOOLS),
        Resource.getResource(ResourceType.FOOD));
    }


    public void initializeResources(){

    }

    @FXML
    private void _handleDisplayResources() {
        Resource.displayResources();
    }
}
