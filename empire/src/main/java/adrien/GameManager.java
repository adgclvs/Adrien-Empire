package adrien;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingFactory;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class GameManager{

    private static final int TICK_DURATION = 1000;

    public static final int WIDTH = 35;
    public static final int HEIGHT = 35;

    private GameTimer gameTimer;
    public static int mapWidth;
    public static int mapHeight;

    /**
     * Constructor for GameManager
     */
    public GameManager() {
        MapManager.getInstance();
        Resource.getInstance();
        Inhabitants.getInstance();
        this.gameTimer = new GameTimer(TICK_DURATION);
    }

/**********************************GETTER********************************************** */
    
    /**
     * @return the mapWidth
     */
    public int getMapWidth() {
        return mapWidth;
    }

    /**
     * @return the mapHeight
     */
    public int getMapHeight() {
        return mapHeight;
    }

/**********************************TIMER********************************************** */

    /**
     * Start the game timer
     */
    public void startGame() {
        gameTimer.addTickListener(this::gameTick);
        gameTimer.start();
    }

    /**
     * Stop the game timer
     */
    public void stopGame() {
        gameTimer.stop();
    }

    /**
     * Method called at each tick of the game timer
     * Updates resources, buildings and inhabitants
     */
    public void gameTick() {
        System.out.println("Tick executed.");
        if (gameTimer.getCurrentTick() % 24 == 0 && gameTimer.getCurrentTick() > 0) {
            updateResourceProduction();
            updateResourceConsumption();
            updateEatingInhabitants();
            System.out.println("Resources updated (24-hour cycle).");
        }
        updateBuildingStatuses();
    }

/**********************************UPDATES********************************************** */

    /**
     * Update the production of resources
     */
    private void updateResourceProduction() {
        for (Building building : MapManager.getInstance().getAllBuildings()) {
            if(building.getCurrentWorkers() > 0){
                if (building.isProducing()) {
                    building.produceResources();
                    System.out.println("Resources produced by: " + building.getType());
                }
                building.setProducing(true);
            }
        }
    }

    /**
     * Update the consumption of resources
     */
    private void updateResourceConsumption() {
        for (Building building : MapManager.getInstance().getAllBuildings()) {
            if (building.isOperational() && building.getCurrentWorkers() > 0) {
                building.consumeResources();
                System.out.println("Resources consumed by: " + building.getType());
            }
        }
    }

    /**
     * Update the status of buildings
     */
    private void updateBuildingStatuses() {
        for (Building building : MapManager.getInstance().getAllBuildings()) {
            if (building.getConstructionTimeRemaining() > 0) {
                building.decrementConstructionTime();
                if (building.getConstructionTimeRemaining() <= 0) {
                    System.out.println("Building completed: " + building.getType());
                    building.setOperational(true);
                    MapManager.getInstance().addInitialInhabitant(building);
                }
            }
        }
    }

    /**
     * Update the number of inhabitants eating
     */
    private void updateEatingInhabitants() {
        ResourceRequirement resourceRequirement = new ResourceRequirement(ResourceType.FOOD, Inhabitants.getNumberInhabitants());
        Resource.getInstance().consumeResource(resourceRequirement);
        
    }

/**********************************BUILDINGS********************************************** */

    /**
     * Add a building to the map
     * @param buildingType
     * @param x
     * @param y
     * @return true if the building was added, false otherwise
     */
    public boolean addBuilding(BuildingType buildingType, int x, int y) {
        Position position = new Position(x, y);
        Building newBuilding = BuildingFactory.createBuilding(buildingType,position);
        boolean result = MapManager.getInstance().addBuilding(position, newBuilding);
        return result;
    }

    /**
     * Remove a building from the map
     * @param x
     * @param y
     * @return true if the building was removed, false otherwise
     */
    public boolean removeBuilding(int x, int y) {
        Position position = new Position(x, y);
        return MapManager.getInstance().removeBuilding(position);
    }

/**********************************WORKERS********************************************** */

    /**
     * Add workers to a building
     * @param building
     * @param workers
     * @return true if the workers were added, false otherwise
     */
    public boolean addWorkers(Building building, int workers) {
       return building.addWorkers(workers);
    }

    /**
     * Remove workers from a building
     * @param building
     * @param workers
     * @return true if the workers were removed, false otherwise
     */
    public boolean removeWorkers(Building building, int workers) {
        return building.removeWorkers(workers);
    }
}