package adrien.controllers;

import java.util.Map;
import java.util.Optional;

import adrien.GameManager;
import adrien.MapManager;
import adrien.Position;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

public class MapController {
    private MainViewController mainViewController;
    private ButtonsController buttonsController;
    private GameManager gameManager;


    private int gridWidth;
    private int gridHeight;
    private double screenWidth;
    private int cellSize;

    @FXML
    private ListView<String> buildingListView;

    @FXML
    private GridPane gridPane;
    
    @FXML
    public void initializeMap(int height, int width) {        

        gridWidth = width;
        gridHeight = height;
        
        screenWidth = Screen.getPrimary().getBounds().getWidth();
        cellSize = (int) (screenWidth / gridWidth);

        MapManager.getInstance(gridWidth, gridHeight);
        Image grassImage = new Image(getClass().getResourceAsStream("/adrien/images/Grass.png"));
        if (grassImage.isError()) {
            System.err.println("Erreur lors du chargement de l'image : " + grassImage.getException());
        } else {
            System.out.println("Image chargée avec succès : " + grassImage);
        }

        
            for (int row = 0; row < gridHeight; row++) {
                for (int col = 0; col < gridWidth; col++) {
                    ImageView grassImageView = new ImageView(grassImage);
                    grassImageView.setFitWidth(cellSize);
                    grassImageView.setFitHeight(cellSize);
                    grassImageView.setPreserveRatio(true);
                    gridPane.add(grassImageView, col, row);
                }
            }
        
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setButtonsController(ButtonsController buttonsController) {
        this.buttonsController = buttonsController;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Building getBuildingAt(int x, int y) {
        // Faire attention ici peu avoir un probleme
        //Pourquoi pas modifier la Position pour avoir deux 
        // coordonnées x et y en haut à gauche et en bas à droite
        // Ca évite d'utiliser la grid
        Position position = new Position(x, y);
        Building building = MapManager.findBuilding(position);
        int building_x = position.getX();
        int building_y = position.getY();
        if (building != null) {
            if (x >= building_x && x < building_x + building.getWidth() && y >= building_y && y < building_y + building.getHeight()) {
                return building; // Retourne le bâtiment trouvé
            }
        }
        return null; // Aucun bâtiment trouvé
    }

    private int[] convertMouseToMapCoordinates(MouseEvent event) {
        double x = event.getSceneX();
        double y = event.getSceneY();
        
        int col = (int) ((x) / cellSize);
        int row = (int) ((y) / cellSize);
        
        return new int[]{col, row};
    }

    private void updateBuildingList() {
        buildingListView.getItems().clear();
        Map<Position, Building> buildings = MapManager.getPositionedBuildings();
        for (Position position : buildings.keySet()) {
            Building building = buildings.get(position);
            buildingListView.getItems().add(building.getName());
        }
    }

    @FXML
public void handleAddBuilding(MouseEvent event) {
    if (buttonsController != null && buttonsController.isSelectingBuildingLocation()) {
        buttonsController.setSelectingBuildingLocation(false);
        buttonsController.updateButtonStyle();

        int[] mapCoordinates = convertMouseToMapCoordinates(event);
        int col = mapCoordinates[0];
        int row = mapCoordinates[1];

        ChoiceDialog<BuildingType> dialog = new ChoiceDialog<>(BuildingType.HOUSE, BuildingType.values());
        dialog.setTitle("Choisir un type de bâtiment");
        dialog.setHeaderText("Sélectionnez le type de bâtiment à ajouter:");
        dialog.setContentText("Type de bâtiment:");

        
        Optional<BuildingType> result = dialog.showAndWait();
        result.ifPresent(buildingType -> {
            if(gameManager.addBuilding(buildingType, col, row)){
                updateBuildingList();
            }else{
                System.out.println("Failed to add building.");
            }
        });
    }
}


    @FXML
    public void handleRemoveBuilding(MouseEvent event) {
        int[] mapCoordinates = convertMouseToMapCoordinates(event);
        String selectedBuilding = buildingListView.getSelectionModel().getSelectedItem();
        if (selectedBuilding != null) {
            
            if (gameManager.removeBuilding(mapCoordinates[0], mapCoordinates[1])) {
                updateBuildingList();
            } else {
                System.out.println("Failed to remove building.");
            }
        }
    }

        // @FXML
    // private void handleAddWorkers() {
    //     String selectedBuilding = buildingListView.getSelectionModel().getSelectedItem();
    //     if (selectedBuilding != null) {
    //         Building buildingToAddWorkers = gameManager.getBuildingByName(selectedBuilding);
    //         if (buildingToAddWorkers != null) {
    //             TextInputDialog dialog = new TextInputDialog();
    //             dialog.setTitle("Ajouter des ouvriers");
    //             dialog.setHeaderText("Entrez le nombre d'ouvriers à ajouter:");
    //             Optional<String> result = dialog.showAndWait();
    //             result.ifPresent(workersToAdd -> {
    //                 try {
    //                     int workers = Integer.parseInt(workersToAdd);
    //                     gameManager.addWorkers(buildingToAddWorkers, workers);
    //                 } catch (NumberFormatException e) {
    //                     System.out.println("Nombre d'ouvriers invalide.");
    //                 }
    //             });
    //         }
    //     }
    // }

    // @FXML
    // private void handleRemoveWorkers() {
    //     String selectedBuilding = buildingListView.getSelectionModel().getSelectedItem();
    //     if (selectedBuilding != null) {
    //         Building buildingToRemoveWorkers = gameManager.getBuildingByName(selectedBuilding);
    //         if (buildingToRemoveWorkers != null) {
    //             TextInputDialog dialog = new TextInputDialog();
    //             dialog.setTitle("Remove Workers");
    //             dialog.setHeaderText("Enter the number of workers to remove:");
    //             Optional<String> result = dialog.showAndWait();
    //             result.ifPresent(workersToRemove -> {
    //                 try {
    //                     int workers = Integer.parseInt(workersToRemove);
    //                     gameManager.removeWorkers(buildingToRemoveWorkers, workers);
    //                 } catch (NumberFormatException e) {
    //                     System.out.println("Invalid number of workers.");
    //                 }
    //             });
    //         }
    //     }
    // }
}
