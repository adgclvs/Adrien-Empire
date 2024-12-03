package adrien.controllers;

import java.util.Map;

import adrien.*;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.util.Duration;

public class MapController implements Observer {

    @FXML
    private StackPane mapPane;

    @FXML
    private GridPane grassGrid;

    @FXML
    private Pane buildingPane; 

    private int gridHeight;
    private int gridWidth;
    private GameManager gameManager;
    private BuildingsController buildingsController;
    private ResourcesController resourcesController;

    public void initialize() {
        if (gameManager != null) {
            gridHeight = gameManager.getMapHeight();
            gridWidth = gameManager.getMapWidth();
            displayMap();
        } else {
            System.out.println("GameManager is not set.");
        }
    }

    public void setResourcesController(ResourcesController controller) {
        this.resourcesController = controller;
        System.out.println("ResourcesController set.");
    }

    public void setBuildingsController(BuildingsController controller) {
        this.buildingsController = controller;
        System.out.println("BuildingsController set.");
    }
    
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        MapManager.getInstance().addObserver(this);
        System.out.println("GameManager set.");
    }

    @Override
    public void update() {
        Platform.runLater(this::displayMap);
    }

    public void handleCellClick(int row, int col) {
        System.out.println("Cell clicked: " + col + ", " + row);
        Position position = new Position(col, row);
        Building building = MapManager.getInstance().findBuilding(position);

        // Si on clique sur un bâtiment et qu'aucun bâtiment n'est sélectionné, alors on affiche les informations du bâtiment
        if (building != null && SharedState.getSelectedBuildingType() == null && building.isOperational()) {
            showBuildingInfo(building, col, row);
            return;
        }

        // Si un bâtiment est sélectionné, alors on le place sur la carte
        BuildingType selectedBuildingType = SharedState.getSelectedBuildingType();
        if (selectedBuildingType != null) {
            boolean added = gameManager.addBuilding(selectedBuildingType, col, row);
            if (added) {
                System.out.println("Building placed at " + col + ", " + row);
                SharedState.setSelectedBuildingType(null); // Clear selected building type
            } else {
                SharedState.setSelectedBuildingType(null); // Clear selected building type
                System.out.println("Cannot place building here.");
            }
        }
    }

    private void showBuildingInfo(Building building, int col, int row) {
        Popup popup = new Popup();
    
        VBox popupContent = new VBox();
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        popupContent.setSpacing(10);
    
        // Barre de titre
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-alignment: center-left;");
        titleBar.setSpacing(10);
    
        Label titleLabel = new Label("Building Info");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    
        Button closeButton = new Button("✖");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 14px;");
        closeButton.setOnAction(event -> popup.hide());
    
        titleBar.getChildren().addAll(titleLabel, closeButton);
    
        // Informations générales
        VBox generalInfoBox = new VBox();
        generalInfoBox.setSpacing(5);
    
        Label typeLabel = new Label("Type: " + building.getType());
        typeLabel.setStyle("-fx-font-size: 14px;");
        generalInfoBox.getChildren().add(typeLabel);
    
        Label workersLabel = new Label("Workers: " + building.getCurrentWorkers());
        workersLabel.setStyle("-fx-font-size: 14px;");
        generalInfoBox.getChildren().add(workersLabel);
    
        Label inhabitantsLabel = new Label("Inhabitants: " + building.getMaxInhabitants());
        inhabitantsLabel.setStyle("-fx-font-size: 14px;");
        generalInfoBox.getChildren().add(inhabitantsLabel);
    
        // Boutons pour ajouter/retirer des travailleurs
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setStyle("-fx-alignment: center;");
    
        Button addButton = new Button("+");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 15; -fx-background-radius: 15;");
        addButton.setPrefWidth(30);
    
        Button removeButton = new Button("-");
        removeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 15; -fx-background-radius: 15;");
        removeButton.setPrefWidth(30);
    
        // Définir la boîte de production ici pour l'utiliser plus tard
        VBox productionBox = new VBox();
        productionBox.setSpacing(10);
        productionBox.setStyle("-fx-alignment: center; -fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5;");
        Label productionTitle = new Label("Production:");
        productionTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        productionBox.getChildren().add(productionTitle);
    
        // Mettre à jour la production
        updateProductionBox(building, productionBox);
    
        addButton.setOnAction(event -> {
            boolean isAdded = gameManager.addWorkers(building, 1);
            if (isAdded) {
                workersLabel.setText("Workers: " + building.getCurrentWorkers());
                updateProductionBox(building, productionBox); // Mise à jour de la production
            }
        });
    
        removeButton.setOnAction(event -> {
            boolean isRemoved = gameManager.removeWorkers(building, 1);
            if (isRemoved) {
                workersLabel.setText("Workers: " + building.getCurrentWorkers());
                updateProductionBox(building, productionBox); // Mise à jour de la production
            }
        });
    
        buttonBox.getChildren().addAll(addButton, removeButton);
    
        // Ajout des composants à la popup
        popupContent.getChildren().addAll(titleBar, generalInfoBox, buttonBox, productionBox);
    
        popup.getContent().add(popupContent);
        popup.setAutoHide(true);
        popup.show(mapPane.getScene().getWindow(), col * 50, row * 50);
    }
    
    private void updateProductionBox(Building building, VBox productionBox) {
        // Supprimer les anciens éléments, sauf le titre
        productionBox.getChildren().remove(1, productionBox.getChildren().size());
    
        if (building.getProduction() != null) {
            for (ResourceRequirement production : building.getProduction()) {
                int totalProduction = production.getQuantity() * building.getCurrentWorkers();
    
                HBox resourceRow = new HBox();
                resourceRow.setSpacing(5);
    
                // Utilisation de l'image en cache
                Image resourceImage = ImageCache.getImage("/adrien/images/resources/" + production.getResourceType().name().toLowerCase() + ".png");
                ImageView resourceImageView = new ImageView(resourceImage);
                resourceImageView.setFitWidth(20);
                resourceImageView.setFitHeight(20);
    
                Label resourceLabel = new Label(totalProduction + " / jour");
                resourceLabel.setStyle("-fx-font-size: 14px;");
    
                resourceRow.getChildren().addAll(resourceImageView, resourceLabel);
                productionBox.getChildren().add(resourceRow);
            }
        } else {
            Label noProductionLabel = new Label("No production available.");
            noProductionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
            productionBox.getChildren().add(noProductionLabel);
        }
    }

    public void displayMap() {
        // Nettoyer le GridPane et le Pane
        grassGrid.getChildren().clear();
        buildingPane.getChildren().clear();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double cellsize = (screenWidth - 100) / gridWidth;

        Image grassImage = ImageCache.getImage("/adrien/images/Grass.png");

        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                // Création d'une ImageView pour chaque cellule d'herbe
                ImageView grassImageView = new ImageView(grassImage);
                grassImageView.setFitWidth(cellsize);
                grassImageView.setFitHeight(cellsize);

                final int row = i;
                final int col = j;

                // Ajouter un gestionnaire de clics directement sur l'image d'herbe
                grassImageView.setOnMouseClicked(event -> {
                    System.out.println("Grass clicked at: " + row + ", " + col);
                    handleCellClick(row, col);
                });

                // Ajouter l'image d'herbe au GridPane
                grassGrid.add(grassImageView, j, i);

                // Vérifier s'il y a un bâtiment à la position actuelle
                Position position = new Position(j, i);
                Building building = MapManager.getInstance().findBuilding(position);

                if (building != null && building.getOrigin().equals(position)) {
                    // Ajouter l'observateur pour mettre à jour la carte lorsque l'état opérationnel change

                    // Créer une ImageView pour le bâtiment et la placer au-dessus des cellules d'herbe
                    ImageView buildingImageView = new ImageView(getBuildingImage(building));
                    buildingImageView.setFitWidth(cellsize * building.getWidth());
                    buildingImageView.setFitHeight(cellsize * building.getHeight());

                    // Ajouter un Label pour afficher le temps de construction restant
                    Label timerLabel = new Label();
                    timerLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-padding: 2; -fx-border-color: black;");
                    timerLabel.setLayoutX(j * cellsize);
                    timerLabel.setLayoutY(i * cellsize - 20); // Ajustez la position du label selon vos besoins

                    Timeline timeline = new Timeline();
                    if (!building.isOperational()) {
                        // Ajouter un Timeline pour mettre à jour le timerLabel
                        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
                            int remainingTime = building.getConstructionTimeRemaining();
                            if (remainingTime > 0) {
                                timerLabel.setText(remainingTime + " seconds");
                            }
                        }));
                        timeline.setCycleCount(Timeline.INDEFINITE);
                        timeline.play();
                    }else {
                        timeline.stop(); // Arrêter le Timeline lorsque le temps est écoulé
                        Platform.runLater(() -> buildingPane.getChildren().remove(timerLabel)); // Supprimer le timer en toute sécurité sur le thread UI
                    }

                    // Définir les coordonnées du bâtiment pour le placer correctement au-dessus des cellules
                    buildingImageView.setLayoutX(j * cellsize);
                    buildingImageView.setLayoutY(i * cellsize);

                    // Gestion des clics sur le bâtiment
                    buildingImageView.setOnMouseClicked(event -> {
                        System.out.println("Building clicked at: " + row + ", " + col);
                        handleCellClick(row, col);
                    });

                    // Ajouter le bâtiment et le timer au Pane
                    buildingPane.getChildren().addAll(buildingImageView, timerLabel);
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
        String imagePath;
        if (building.isOperational()) {
            imagePath = "/adrien/images/buildings/" + building.getType().toString().toLowerCase() + ".png";
        } else {
            imagePath = "/adrien/images/buildings/inprogress.png";
        }

        return ImageCache.getImage(imagePath);
    }
}