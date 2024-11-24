package adrien;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adrien.buildings.BuildingsManager.Building;
import adrien.controllers.MapController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

public class MapManager extends Observable {
    public static int width;
    public static int height;
    private static boolean[][] grid;
    private static Map<Position, Building> buildings;
    private static MapManager instance;

    /*************************************CONSTRUCTOR***************************************** */
    private MapManager(int width, int height) {
        MapManager.width = width;
        MapManager.height = height;
        MapManager.grid = new boolean[height][width];
        MapManager.buildings = new HashMap<>();
    }

    /*************************************INSTANCE***************************************** */

    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager(40, 20);
        }
        return instance;
    }

    /*************************************GETTER***************************************** */

    public static Map<Position, Building> getPositionnedBuildings() {
        return buildings;
    }

    public static Building[] getAllBuildings() {
        return buildings.values().toArray(new Building[0]);
    }

    public static Building findBuilding(Position position) {
        return buildings.get(position);
    }

    public static Image getBuildingImage(Building building) {
        String imagePath = "/adrien/images/buildings/" + building.getType().toString().toLowerCase() + ".png";
        return new Image(MapManager.class.getResourceAsStream(imagePath));
    }

    /*************************************BUILDINGS***************************************** */

    public static boolean addBuilding(Position position, Building building) {
        if (!isSpaceAvailable(building, position)) return false;
        buildings.put(position, building);
        occupySpace(building, position);
        instance.notifyObservers();
        return true;
    }

    public static boolean removeBuilding(Position position) {
        Building building = buildings.remove(position);
        if (building != null) {
            deOccupySpace(building, position);
            instance.notifyObservers();
            return true;
        }
        instance.notifyObservers();
        return false;
    }

    /*************************************SPACE***************************************** */

    public static boolean isSpaceAvailable(Building building, Position position) {
        int x = position.getX();
        int y = position.getY();
        System.out.println("qmsojghqposhgqjsjddvsjqvsdl");


        System.out.println("building.getWidth(): " + building.getWidth());
        System.out.println("building.getHeight(): " + building.getHeight());

        System.out.println(width);
        System.out.println(height);
        if (x < 0 || y < 0 || x + building.getWidth() > width || y + building.getHeight() > height) {
            return false;
        }

    
        // Vérifier si l'espace requis pour le bâtiment est libre
        for (int i = 0; i < building.getHeight(); i++) {
            for (int j = 0; j < building.getWidth(); j++) {
                if (grid[y + i][x + j]) {
                    return false;
                }
            }
        }
    
        return true;
    }

    public static void occupySpace(Building building, Position position) {
        int x = position.getX();
        int y = position.getY();
        for (int i = 0; i < building.getHeight(); i++) {
            for (int j = 0; j < building.getWidth(); j++) {
                grid[y + i][x + j] = true;
            }
        }
    }

    public static void deOccupySpace(Building building, Position position) {
        int x = position.getX();
        int y = position.getY();
        for (int i = 0; i < building.getHeight(); i++) {
            for (int j = 0; j < building.getWidth(); j++) {
                grid[y + i][x + j] = false;
            }
        }
    }

    /*************************************DISPLAY***************************************** */

    public static void displayMap(GridPane mapPane, MapController controller) {
    mapPane.getChildren().clear();
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double cellsize = (screenWidth - 100) / width;

    Image grassImage = new Image(MapManager.class.getResourceAsStream("/adrien/images/Grass.png"));
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            ImageView imageView;
            if (grid[i][j]) {
                Position position = new Position(j, i);
                Building building = buildings.get(position);
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

            // Ajouter un gestionnaire d'événements
            int finalRow = i;
            int finalCol = j;
            imageView.setOnMouseClicked(event -> controller.handleCellClick(finalRow, finalCol));

            mapPane.add(imageView, j, i);
        }
    }
}
}
