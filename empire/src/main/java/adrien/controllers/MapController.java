package adrien.controllers;

import adrien.GameManager;
import adrien.MapManager;
import adrien.Position;
import adrien.SharedState;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingFactory;
import adrien.buildings.BuildingsManager.BuildingType;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

public class MapController {

    @FXML
    private GridPane mapPane;

    private int gridHeight;
    private int gridWidth;
    private GameManager gameManager;
    private BuildingsController buildingsController;

    public void initialize() {
        if (gameManager != null) {
            gridHeight = gameManager.getMapHeight();
            gridWidth = gameManager.getMapWidth();
            displayMap();
        }else{
            System.out.println("GameManager is not set.");
        }
    }

    public void setBuildingsController(BuildingsController controller) {
        this.buildingsController = controller;
        System.out.println("BuildingsController set.");
    }
    
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        System.out.println("GameManager set.");
    }

    public void handleCellClick(int row, int col) {
    BuildingType selectedBuildingType = SharedState.getSelectedBuildingType();
    System.out.println("Selected building type: " + selectedBuildingType);
    if (selectedBuildingType != null) {
        boolean added = gameManager.addBuilding(selectedBuildingType, col, row);
        System.out.println(added);
        if (added) {
            System.out.println("Building placed at " + col + ", " + row);
            SharedState.setSelectedBuildingType(null); // Clear selected building type
            displayMap(); // Mettre à jour la carte après avoir ajouté un bâtiment
        } else {
            System.out.println("Cannot place building here.");
        }
    } else {
        System.out.println("No building type selected.");
    }
}

    public void displayMap() {
        mapPane.getChildren().clear();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double cellsize = (screenWidth - 100) / gridWidth;

        Image grassImage = new Image(MapManager.class.getResourceAsStream("/adrien/images/Grass.png"));
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                ImageView imageView;
                Position position = new Position(j, i);
                if (MapManager.findBuilding(position) != null) {
                    Building building = MapManager.findBuilding(position);
                    if (building != null) {
                        imageView = new ImageView(getBuildingImage(building));
                    } else {
                        imageView = new ImageView(grassImage);
                    }
                } else {
                    imageView = new ImageView(grassImage);
                }
                imageView.setFitWidth(cellsize);
                imageView.setFitHeight(cellsize);
                final int row = i;
                final int col = j;
                imageView.setOnMouseClicked(event -> handleCellClick(row, col)); // Gérer le clic
                mapPane.add(imageView, j, i);
            }
        }
    }

    private Image getBuildingImage(Building building) {
        String imagePath = "/adrien/images/buildings/" + building.getType().toString().toLowerCase() + ".png";
        Image image = new Image(MapManager.class.getResourceAsStream(imagePath));
        if (image.isError()) {
            System.out.println("Error loading image: " + imagePath);
        }
        return image;
    }

    
}