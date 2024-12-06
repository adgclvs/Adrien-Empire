package adrien.controllers;

import adrien.*;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Shear;
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
    private Pane decorPane;

    private GameManager gameManager;

    public void initialize() {
        if (gameManager != null) {
            initializeDecor();
            displayMap();
        } else {
            System.out.println("GameManager is not set.");
        }
    }

    @Override
    public void update() {
        Platform.runLater(this::displayMap);
    }
    
    /**
     * Set the GameManager instance
     * @param gameManager
     */
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        MapManager.getInstance().addObserver(this);
        System.out.println("GameManager set.");
    }

    /**
     * Handle the click event on a cell in the map
     * @param row
     * @param col
     */
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
            } else {
                System.out.println("Cannot place building here.");
            }
            SharedState.setSelectedBuildingType(null); // Clear selected building type
        }
    }

/*********************************************POP UP********************************************** */

    /**
     * Show the building information popup
     * @param building
     * @param col
     * @param row
     */
    private void showBuildingInfo(Building building, int col, int row) {
        Popup popup = new Popup();
        VBox popupContent = createPopupContainer();
    
        // Ajouter les différentes sections
        HBox titleBar = createTitleBar(popup);
        VBox generalInfoBox = createGeneralInfoBox(building);
        VBox productionBox = createProductionSection(building);
        HBox buttonBox = createActionButtons(building, generalInfoBox, productionBox);
        Button deleteButton = createDeleteButton(popup, col, row);
    
        // Ajouter tous les éléments à la popup
        popupContent.getChildren().addAll(titleBar, generalInfoBox, buttonBox);
        if (building.getProduction() != null && building.getProduction().length != 0) {
            popupContent.getChildren().add(productionBox);
        }
        popupContent.getChildren().add(deleteButton);
    
        popup.getContent().add(popupContent);
        popup.setAutoHide(true);
    
        // Positionnement isométrique
        positionPopup(popup, col, row);
    }
    
    // 1. Création du conteneur principal
    private VBox createPopupContainer() {
        VBox popupContent = new VBox();
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        popupContent.setSpacing(10);
        return popupContent;
    }
    
    // 2. Création de la barre de titre
    private HBox createTitleBar(Popup popup) {
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-alignment: center-left;");
        titleBar.setSpacing(10);
    
        Label titleLabel = new Label("Building Info");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    
        Button closeButton = new Button("✖");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 14px;");
        closeButton.setOnAction(event -> popup.hide());
    
        titleBar.getChildren().addAll(titleLabel, closeButton);
        return titleBar;
    }
    
    // 3. Création de la section des informations générales
    private VBox createGeneralInfoBox(Building building) {
        VBox generalInfoBox = new VBox();
        generalInfoBox.setSpacing(5);
    
        Label typeLabel = new Label(building.getType().toString());
        typeLabel.setStyle("-fx-font-size: 14px;");
        generalInfoBox.getChildren().add(typeLabel);
    
        Label workersLabel = new Label("Workers: " + building.getCurrentWorkers());
        workersLabel.setStyle("-fx-font-size: 14px;");
        generalInfoBox.getChildren().add(workersLabel);
    
        Label inhabitantsLabel = new Label("Inhabitants: " + building.getMaxInhabitants());
        inhabitantsLabel.setStyle("-fx-font-size: 14px;");
        generalInfoBox.getChildren().add(inhabitantsLabel);
    
        int availableWorkers = Inhabitants.getInstance().getNumberInhabitants() - Inhabitants.getInstance().getNumberWorkers();
        Label availableWorkersLabel = new Label("Available Workers: " + availableWorkers);
        availableWorkersLabel.setStyle("-fx-font-size: 14px;");
        generalInfoBox.getChildren().add(availableWorkersLabel);
    
        return generalInfoBox;
    }
    
    // 4. Création de la section de production
    private VBox createProductionSection(Building building) {
        VBox productionBox = new VBox();
        productionBox.setSpacing(10);
        productionBox.setStyle("-fx-alignment: center; -fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5;");
    
        Label productionTitle = new Label("Production:");
        productionTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        productionBox.getChildren().add(productionTitle);
    
        updateProductionBox(building, productionBox);
        return productionBox;
    }
    
    // 5. Création des boutons d'action (ajouter/retirer des travailleurs)
    private HBox createActionButtons(Building building, VBox generalInfoBox, VBox productionBox) {
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setStyle("-fx-alignment: center;");
    
        Button addButton = new Button("+");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 15; -fx-background-radius: 15;");
        addButton.setPrefWidth(30);
    
        Button removeButton = new Button("-");
        removeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 15; -fx-background-radius: 15;");
        removeButton.setPrefWidth(30);
    
        if (building.getProduction() == null || building.getProduction().length == 0) {
            addButton.setDisable(true);
            removeButton.setDisable(true);
        }
    
        addButton.setOnAction(event -> {
            boolean isAdded = gameManager.addWorkers(building, 1);
            if (isAdded) {
                updateWorkerInfo(building, generalInfoBox, productionBox);
            }
        });
    
        removeButton.setOnAction(event -> {
            boolean isRemoved = gameManager.removeWorkers(building, 1);
            if (isRemoved) {
                updateWorkerInfo(building, generalInfoBox, productionBox);
            }
        });
    
        buttonBox.getChildren().addAll(addButton, removeButton);
        return buttonBox;
    }
    
    // 6. Création du bouton de suppression
    private Button createDeleteButton(Popup popup, int col, int row) {
        Button deleteButton = new Button("Delete Building");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 15; -fx-background-radius: 15;");
        deleteButton.setOnAction(event -> {
            gameManager.removeBuilding(col, row);
            popup.hide();
            displayMap();
        });
        return deleteButton;
    }
    
    // 7. Mise à jour des informations sur les travailleurs et la production
    private void updateWorkerInfo(Building building, VBox generalInfoBox, VBox productionBox) {
        Label workersLabel = (Label) generalInfoBox.getChildren().get(1);
        workersLabel.setText("Workers: " + building.getCurrentWorkers());
    
        updateProductionBox(building, productionBox);
    
        Label availableWorkersLabel = (Label) generalInfoBox.getChildren().get(3);
        int availableWorkers = Inhabitants.getInstance().getNumberInhabitants() - Inhabitants.getInstance().getNumberWorkers();
        availableWorkersLabel.setText("Available Workers: " + availableWorkers);
    }
    
    // 8. Positionnement de la popup
    private void positionPopup(Popup popup, int col, int row) {
        double tileWidth = 50;
        double tileHeight = 25;
        double offsetX = 875;
        double offsetY = 25;
    
        double buildingX = offsetX + (col - row) * (tileWidth / 2);
        double buildingY = offsetY + (col + row) * (tileHeight / 2);
    
        popup.show(mapPane.getScene().getWindow(), buildingX + tileWidth, buildingY);
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
        for (int row = -decorMargin; row < GameManager.HEIGHT + decorMargin; row++) {
            for (int col = -decorMargin; col < GameManager.WIDTH + decorMargin; col++) {
                // Ne pas ajouter de décor à l'intérieur de la grille principale
                if (row >= 0 && row < GameManager.HEIGHT && col >= 0 && col < GameManager.WIDTH) {
                    continue;
                }
                // Calcul des positions isométriques pour le décor
                double x = (col - row) * (tileWidth / 2) + offsetX;
                double y = (col + row) * (tileHeight / 2) + offsetY;

            // Ajouter la tuile d'herbe au décor
            ImageView decorImageView = new ImageView(grassImage);
            decorImageView.setFitWidth(tileWidth);
            decorImageView.setFitHeight(tileHeight);

            // Appliquer une légère transformation de cisaillement pour incliner l'image
            Shear shear = new Shear(0.1, 0); // Cisaillement horizontal de 0.1 (ajustez selon vos besoins)
            decorImageView.getTransforms().add(shear);

            decorImageView.setLayoutX(x);
            decorImageView.setLayoutY(y);
            decorPane.getChildren().add(decorImageView);
            }
        }

        // Définir la taille fixe du décorPane pour éviter d'empiéter sur les autres éléments
        decorPane.setPrefSize((GameManager.WIDTH + 2 * decorMargin) * tileWidth, (GameManager.HEIGHT + 2 * decorMargin) * tileHeight);
    }

/*********************************************DISPLAY MAP****************************************************** */

    public void displayMap() {
        // Nettoyer les conteneurs
        clearMapContainers();
    
        // Ajouter les panneaux de base
        addBaseLayers();
    
        // Dimensions des cellules isométriques
        double tileWidth = 50;
        double tileHeight = 25;
    
        // Calcul des offsets pour centrer la grille
        double offsetX = 875;
        double offsetY = 25;
    
        // Parcourir la grille et ajouter les éléments
        for (int row = 0; row < GameManager.HEIGHT; row++) {
            for (int col = 0; col < GameManager.WIDTH; col++) {
                double x = (col - row) * (tileWidth / 2) + offsetX;
                double y = (col + row) * (tileHeight / 2) + offsetY;
    
                // Ajouter une tuile d'herbe
                addGrassTile(x, y);
    
                // Ajouter un bâtiment, si présent
                addBuildingIfPresent(col, row, x, y, tileWidth, tileHeight);
    
                // Ajouter une zone cliquable transparente
                addClickArea(col, row, x, y, tileWidth, tileHeight);
            }
        }
    }
    
    // 1. Nettoyer les conteneurs
    private void clearMapContainers() {
        grassGrid.getChildren().clear();
        buildingPane.getChildren().clear();
        mapPane.getChildren().clear();
    }
    
    // 2. Ajouter les couches de base
    private void addBaseLayers() {
        mapPane.getChildren().add(decorPane);  // Ajouter le décor en premier
        mapPane.getChildren().add(grassGrid);
        mapPane.getChildren().add(buildingPane);
    }
    
    // 3. Ajouter une tuile d'herbe
    private void addGrassTile(double x, double y) {
        Image grassImage = ImageCache.getImage("/adrien/images/Grass.png");
        ImageView grassImageView = new ImageView(grassImage);
        grassImageView.setFitWidth(50);
        grassImageView.setFitHeight(25);
        grassImageView.setLayoutX(x);
        grassImageView.setLayoutY(y);
        grassGrid.getChildren().add(grassImageView);
    }
    
    // 4. Ajouter un bâtiment, s'il existe
    private void addBuildingIfPresent(int col, int row, double x, double y, double tileWidth, double tileHeight) {
        Position position = new Position(col, row);
        Building building = MapManager.getInstance().findBuilding(position);
    
        if (building != null && building.getOrigin().equals(position)) {
            double buildingX = x;
            double buildingY = y;
    
            // Créer et positionner l'image du bâtiment
            ImageView buildingImageView = new ImageView(getBuildingImage(building));
            buildingImageView.setFitWidth(tileWidth * building.getWidth());
            buildingImageView.setFitHeight(tileHeight * building.getHeight());
            buildingImageView.setLayoutX(buildingX - (building.getWidth() - 1) * (tileWidth / 2));
            buildingImageView.setLayoutY(buildingY - (building.getHeight() - 1) * (tileHeight / 2));
    
            // Gestion des clics sur le bâtiment
            buildingImageView.setOnMouseClicked(event -> handleCellClick(row, col));
    
            buildingPane.getChildren().add(buildingImageView);
    
            // Afficher le timer si le bâtiment est en construction
            if (!building.isOperational()) {
                addConstructionTimer(building, buildingX, buildingY);
            }
        }
    }
    
    // 5. Ajouter un timer de construction
    private void addConstructionTimer(Building building, double buildingX, double buildingY) {
        Label timerLabel = new Label();
        timerLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-padding: 2; -fx-border-color: black;");
        timerLabel.setLayoutX(buildingX);
        timerLabel.setLayoutY(buildingY - 40);
    
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
    
    // 6. Ajouter une zone cliquable transparente
    private void addClickArea(int col, int row, double x, double y, double tileWidth, double tileHeight) {
        javafx.scene.shape.Rectangle clickArea = new javafx.scene.shape.Rectangle(tileWidth, tileHeight);
        clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
        clickArea.setLayoutX(x);
        clickArea.setLayoutY(y);
        clickArea.setOnMouseClicked(event -> handleCellClick(row, col));
        buildingPane.getChildren().add(clickArea);
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
