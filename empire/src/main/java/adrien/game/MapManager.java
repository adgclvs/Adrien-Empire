package adrien.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.Position;
import adrien.observers.Observable;
import javafx.scene.image.Image;

public class MapManager extends Observable {

    private boolean[][] grid;
    private Map<Position, Building> buildings;
    private static MapManager instance;

    /*************************************CONSTRUCTOR***************************************** */

    /**
     * Constructor for MapManager
     */
    private MapManager() {
        this.grid = new boolean[GameManager.HEIGHT][GameManager.WIDTH];
        this.buildings = new HashMap<>();
    }

    /*************************************INSTANCE***************************************** */

    /**
     * Get the instance of the MapManager
     */
    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    /*************************************GETTER***************************************** */

    /**
     * @return buildings with their position using a map
     */
    public Map<Position, Building> getPositionnedBuildings() {
        return buildings;
    }

    /**
     * @return all buildings in a table
     */
    public Building[] getAllBuildings() {
        Set<Building> uniqueBuildings = new HashSet<>(buildings.values());
        return uniqueBuildings.toArray(new Building[0]);
    }

    /**
     * @param position
     * @return the building at the given position
     */
    public Building findBuilding(Position position) {
        return buildings.get(position);
    }

    /**
     * @param building
     * @return the image of the building
     */
    public Image getBuildingImage(Building building) {
        String imagePath = "/adrien/images/buildings/" + building.getType().toString().toLowerCase() + ".png";
        return new Image(MapManager.class.getResourceAsStream(imagePath));
    }

    /*************************************BUILDINGS***************************************** */

    /**
     * Add a building to the map
     * @param position
     * @param building
     * @return true if the building was added, false otherwise
     */
    public boolean addBuilding(Position position, Building building) {
        if (!isSpaceAvailable(building, position)){
            System.out.println("Not enough space");
            return false;
        } 

        if (!building.costBuildingResources()){
            System.out.println("Not enough resources");
            return false; 
        } 
        
        for (int i = 0; i < building.getHeight(); i++) {
            for (int j = 0; j < building.getWidth(); j++) {
                Position newPosition = new Position(position.getX() + j, position.getY() + i);
                buildings.put(newPosition, building);
            }
        }
        occupySpace(building, position);
        this.notifyObservers();
        return true;
    }

    /**
     * Add an initial inhabitant to the building
     * @param building
     * @return true if the inhabitant was added, false otherwise
     */
    public boolean addInitialInhabitant(Building building) {
        int numberOfInhabitants = building.getMaxInhabitants();
        Inhabitants.getInstance().addInhabitants(numberOfInhabitants);
        this.notifyObservers(); 
        return true;
    }

    /**
     * Remove a building from the map
     * @param position
     * @return true if the building was removed, false otherwise
     */
    public boolean removeBuilding(Position position) {
        Building building = buildings.get(position);
        if (building != null) {
            Position originPos = building.getOrigin();
            for(int row = 0; row < building.getHeight(); row++) {
                for(int col = 0; col < building.getWidth(); col++) {
                    Position newPosition = new Position(originPos.getX() + col, originPos.getY() + row);
                    buildings.remove(newPosition);
                }
            }
            deOccupySpace(building, originPos);
            int numberOfInhabitants = building.getMaxInhabitants();
            Inhabitants.getInstance().removeInhabitants(numberOfInhabitants);
            this.notifyObservers();
            return true;
        }
        return false;
    }

    /*************************************SPACE***************************************** */

    /**
     * Check if the space is available for a building
     * @param building
     * @param position
     * @return true if the space is available, false otherwise
     */
    public boolean isSpaceAvailable(Building building, Position position) {
        int x = position.getX();
        int y = position.getY();

        if (x < 0 || y < 0 || x + building.getWidth() > GameManager.WIDTH || y + building.getHeight() > GameManager.HEIGHT) {
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

    /**
     * Occupy the space for a building
     * @param building
     * @param position
     */
    public void occupySpace(Building building, Position position) {
        int x = position.getX();
        int y = position.getY();
        for (int i = 0; i < building.getHeight(); i++) {
            for (int j = 0; j < building.getWidth(); j++) {
                grid[y + i][x + j] = true;
            }
        }
    }

    /**
     * De-occupy the space for a building
     * @param building
     * @param position
     */
    public void deOccupySpace(Building building, Position position) {
        int x = position.getX();
        int y = position.getY();
        for (int i = 0; i < building.getHeight(); i++) {
            for (int j = 0; j < building.getWidth(); j++) {
                grid[y + i][x + j] = false;
            }
        }
    }

}
