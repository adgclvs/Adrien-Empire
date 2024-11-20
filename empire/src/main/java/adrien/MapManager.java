package adrien;

import java.util.HashMap;
import java.util.Map;

import adrien.buildings.BuildingsManager.Building;

public class MapManager {
    public static int width;
    public static int height;
    private static boolean[][] grid;
    private static Map<Position, Building> buildings;
    private static MapManager instance;

    private MapManager(int width, int height) {
        MapManager.width = width;
        MapManager.height = height;
        MapManager.grid = new boolean[height][width];
        MapManager.buildings = new HashMap<>();
    }

    public static MapManager getInstance(int width, int height) {
        if (instance == null) {
            instance = new MapManager(width, height);
        }
        return instance;
    }

    public static Map<Position, Building> getPositionedBuildings() {
        return buildings;
    }

    public static boolean addBuilding(Position position, Building building) {
        if (!isSpaceAvailable(building, position)) return false;
        buildings.put(position, building);
        occupySpace(building, position);
        return true;
    }

    public static boolean removeBuilding(Position position) {
        Building building = buildings.remove(position);
        if (building != null) {
            deOccupySpace(building, position);
            return true;
        }
        return false;
    }

    private static boolean isSpaceAvailable(Building building, Position position) {
        int x = position.getX();
        int y = position.getY();
        for (int i = 0; i < building.getHeight(); i++) {
            for (int j = 0; j < building.getWidth(); j++) {
                if (x + j >= width || y + i >= height || grid[y + i][x + j] == true) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Building findBuilding(Position position) {
        return buildings.get(position);
    }

    private static void occupySpace(Building building, Position position) {
        int x = position.getX();
        int y = position.getY();
        for (int i = 0; i < building.getHeight(); i++) {
            for (int j = 0; j < building.getWidth(); j++) {
                grid[y + i][x + j] = true;
            }
        }
    }

    private static void deOccupySpace(Building building, Position position) {
        int x = position.getX();
        int y = position.getY();
        for (int i = 0; i < building.getHeight(); i++) {
            for (int j = 0; j < building.getWidth(); j++) {
                grid[y + i][x + j] = false;
            }
        }
    }

    public static void displayMap() {
        for (Building building : buildings.values()) {
            System.out.println("Building: " + building.getName() + ", Size: " + building.getWidth() + "x" + building.getHeight());
        }
    }
}
