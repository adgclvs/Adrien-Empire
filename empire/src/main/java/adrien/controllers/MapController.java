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
import javafx.util.Duration;

public class MapController implements Observer {

    @FXML
    private StackPane mapPane;

    @FXML
    private Pane grassGrid;

    @FXML
    private Pane buildingPane;

    @FXML
    private Pane decorPane;  // Nouveau Pane pour le décor

    private GameManager gameManager;
    private BuildingsController buildingsController;
    private ResourcesController resourcesController;

    public void initialize() {
        if (gameManager != null) {
            initializeDecor();
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

    public void initializeDecor() {
        // Charger l'image d'herbe
        Image grassImage = ImageCache.getImage("/adrien/images/Tree.png");

        // Dimensions des cellules isométriques
        double tileWidth = 50; // Largeur de la tuile isométrique
        double tileHeight = 25; // Hauteur de la tuile isométrique

        // Calcul des offsets pour centrer la grille
        double offsetX = 875; // Décalage horizontal
        double offsetY = 25; // Décalage vertical (optionnel)

        // Ajouter des tuiles d'herbe autour de la grille comme décor
        int decorMargin = 25;  // Nombre de tuiles d'herbe autour de la grille
        for (int row = -decorMargin; row < GameManager.height + decorMargin; row++) {
            for (int col = -decorMargin; col < GameManager.width + decorMargin; col++) {
                // Ne pas ajouter de décor à l'intérieur de la grille principale
                if (row >= 0 && row < GameManager.height && col >= 0 && col < GameManager.width) {
                    continue;
                }
                // Calcul des positions isométriques pour le décor
                double x = (col - row) * (tileWidth / 2) + offsetX;
                double y = (col + row) * (tileHeight / 2) + offsetY;

                // Ajouter la tuile d'herbe au décor
                ImageView decorImageView = new ImageView(grassImage);
                decorImageView.setFitWidth(tileWidth);
                decorImageView.setFitHeight(tileHeight);
                decorImageView.setLayoutX(x);
                decorImageView.setLayoutY(y);
                decorPane.getChildren().add(decorImageView);
            }
        }

        // Définir la taille fixe du décorPane pour éviter d'empiéter sur les autres éléments
        decorPane.setPrefSize((GameManager.width + 2 * decorMargin) * tileWidth, (GameManager.height + 2 * decorMargin) * tileHeight);
    }

    public void displayMap() {
        // Nettoyer les conteneurs
        grassGrid.getChildren().clear();
        buildingPane.getChildren().clear();
        mapPane.getChildren().clear();
    
        // Ajouter les éléments au mapPane (le StackPane)
        mapPane.getChildren().add(decorPane);  // Ajouter le décor en premier
        mapPane.getChildren().add(grassGrid);
        mapPane.getChildren().add(buildingPane);
    
        // Dimensions des cellules isométriques
        double tileWidth = 50; // Largeur de la tuile isométrique
        double tileHeight = 25; // Hauteur de la tuile isométrique
    
        // Calcul des offsets pour centrer la grille
        double offsetX = 875; // Décalage horizontal
        double offsetY = 25; // Décalage vertical (optionnel)
    
        // Charger l'image d'herbe
        Image grassImage = ImageCache.getImage("/adrien/images/Grass.png");
    
        // Parcourir la grille principale
        for (int row = 0; row < GameManager.height; row++) {
            for (int col = 0; col < GameManager.width; col++) {
                final int r = row;
                final int c = col;
    
                // Calcul des positions isométriques
                double x = (col - row) * (tileWidth / 2) + offsetX;
                double y = (col + row) * (tileHeight / 2) + offsetY;
    
                // --- 1. Ajouter une tuile d'herbe ---
                ImageView grassImageView = new ImageView(grassImage);
                grassImageView.setFitWidth(tileWidth);
                grassImageView.setFitHeight(tileHeight);
                grassImageView.setLayoutX(x); // Position X avec offset
                grassImageView.setLayoutY(y); // Position Y avec offset

                // Ajouter la tuile d'herbe au grassGrid
                grassGrid.getChildren().add(grassImageView);
    
                // --- Vérifier la présence d'un bâtiment ---
                Position position = new Position(col, row);
                Building building = MapManager.getInstance().findBuilding(position);

                if (building != null && building.getOrigin().equals(position)) {
                    // Calcul des positions isométriques pour le bâtiment
                    double buildingX = offsetX + (col - row) * (tileWidth / 2);
                    double buildingY = offsetY + (col + row) * (tileHeight / 2);

                    // Créer et positionner le bâtiment
                    ImageView buildingImageView = new ImageView(getBuildingImage(building));
                    buildingImageView.setFitWidth(tileWidth * building.getWidth());
                    buildingImageView.setFitHeight(tileHeight * building.getHeight());

                    // Ajuster la position pour tenir compte de la hauteur du bâtiment
                    buildingImageView.setLayoutX(buildingX - (building.getWidth() - 1) * (tileWidth / 2));
                    buildingImageView.setLayoutY(buildingY - (building.getHeight() - 1) * (tileHeight / 2));

                    // Gestion des clics sur le bâtiment
                    buildingImageView.setOnMouseClicked(event -> {
                        System.out.println("Building clicked at: " + r + ", " + c);
                        handleCellClick(r, c);
                    });

                    // Ajouter le bâtiment au panneau au-dessus des tuiles d'herbe
                    buildingPane.getChildren().add(buildingImageView);

                    // --- 3. Affichage du timer pour les bâtiments en construction ---
                    if (!building.isOperational()) {
                        Label timerLabel = new Label();
                        timerLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-padding: 2; -fx-border-color: black;");
                        timerLabel.setLayoutX(buildingX);
                        timerLabel.setLayoutY(buildingY - 40); // Position au-dessus du bâtiment
    
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                            int remainingTime = building.getConstructionTimeRemaining();
                            if (remainingTime > 0) {
                                timerLabel.setText(remainingTime + " seconds");
                            } else {
                                buildingPane.getChildren().remove(timerLabel);
                            }
                        }));
                        timeline.setCycleCount(Timeline.INDEFINITE);
                        timeline.play();
    
                        buildingPane.getChildren().add(timerLabel);
                    }
                }
    
                // --- 4. Ajouter une zone cliquable transparente ---
                javafx.scene.shape.Rectangle clickArea = new javafx.scene.shape.Rectangle(tileWidth, tileHeight);
                clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                clickArea.setLayoutX(x);
                clickArea.setLayoutY(y);
                clickArea.setOnMouseClicked(event -> handleCellClick(r, c));
    
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
