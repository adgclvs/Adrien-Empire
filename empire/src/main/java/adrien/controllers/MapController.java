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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

public class MapController {

    @FXML
    private StackPane mapPane;

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
        System.out.println( "Cell clicked: " + col + ", " + row);
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

@FXML
private GridPane grassGrid; // Le GridPane pour les images d'herbe

@FXML
private Pane buildingPane; // Le Pane pour superposer les bâtiments

public void displayMap() {
    // Nettoyer le GridPane et le Pane
    grassGrid.getChildren().clear();
    buildingPane.getChildren().clear();

    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double cellsize = (screenWidth - 100) / gridWidth;

    Image grassImage = new Image(MapManager.class.getResourceAsStream("/adrien/images/Grass.png"));

    for (int i = 0; i < gridHeight; i++) {
        for (int j = 0; j < gridWidth; j++) {
            // Création d'une ImageView pour chaque cellule d'herbe
            ImageView grassImageView = new ImageView(grassImage);
            grassImageView.setFitWidth(cellsize);
            grassImageView.setFitHeight(cellsize);

            // Ajouter l'image d'herbe au GridPane
            grassGrid.add(grassImageView, j, i);

            // Vérifier s'il y a un bâtiment à la position actuelle
            Position position = new Position(j, i);
            Building building = MapManager.findBuilding(position);

            final int row = i;
            final int col = j;

            if (building != null && building.getOrigin().equals(position)) {
                // Créer une ImageView pour le bâtiment et la placer au-dessus des cellules d'herbe
                ImageView buildingImageView = new ImageView(getBuildingImage(building));
                buildingImageView.setFitWidth(cellsize * building.getWidth());
                buildingImageView.setFitHeight(cellsize * building.getHeight());

                // Définir les coordonnées du bâtiment pour le placer correctement au-dessus des cellules
                buildingImageView.setLayoutX(j * cellsize);
                buildingImageView.setLayoutY(i * cellsize);

                

                // Gestion des clics sur le bâtiment
                
                buildingImageView.setOnMouseClicked(event -> handleCellClick(row, col));

                // Ajouter le bâtiment au Pane
                buildingPane.getChildren().add(buildingImageView);
            }

            // Ajouter un rectangle transparent pour capter les clics sur les cellules d'herbe
            javafx.scene.shape.Rectangle clickArea = new javafx.scene.shape.Rectangle(cellsize, cellsize);
            clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
            clickArea.setLayoutX(j * cellsize);
            clickArea.setLayoutY(i * cellsize);
            clickArea.setOnMouseClicked(event -> handleCellClick(row, col));

            // Ajouter le rectangle transparent au Pane des bâtiments (au-dessus de l'herbe, mais sous le bâtiment)
            buildingPane.getChildren().add(clickArea);
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