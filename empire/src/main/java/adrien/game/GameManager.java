package adrien.game;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingFactory;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.buildings.BuildingsManager.Position;
import adrien.exceptions.BuildingException;
import adrien.exceptions.NoFoodException;
import adrien.exceptions.ResourceConsuptionException;
import adrien.exceptions.ResourceProductionException;
import adrien.exceptions.WorkerException;
import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class GameManager{

    private static final int TICK_DURATION = 1000;

    public static final int WIDTH = 35;
    public static final int HEIGHT = 35;

    private static GameManager instance;

    private GameTimer gameTimer;
    public static int mapWidth;
    public static int mapHeight;

    /**
     * Constructor for GameManager
     */
    private GameManager() {
        MapManager.getInstance();
        Resource.getInstance();
        Inhabitants.getInstance();
        this.gameTimer = new GameTimer(TICK_DURATION);
    }

    /**
     * Get the singleton instance of GameManager
     * @return the instance of GameManager
     */
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
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
        try {

            if (gameTimer.getCurrentTick() % 24 == 0 && gameTimer.getCurrentTick() > 0) {
                updateResourceProduction();
                updateResourceConsumption();
                updateEatingInhabitants();
                System.out.println("Resources updated (24-hour cycle).");
            }
            updateBuildingStatuses();
        } catch (NoFoodException e) {
            System.err.println("Game over: " + e.getMessage());
            stopGame();
        }
    }

/**********************************UPDATES********************************************** */

    /**
     * Update the production of resources
     */
    private void updateResourceProduction() {
        for (Building building : MapManager.getInstance().getAllBuildings()) {
            if(building.getCurrentWorkers() > 0){
                if (building.isProducing()) {
                    try {
                        building.produceResources();
                        System.out.println("Resources produced by: " + building.getType());
                    } catch (ResourceProductionException e) {
                        e.printStackTrace();
                    }
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
                try {
                    building.consumeResources();
                    System.out.println("Resources consumed by: " + building.getType());
                } catch (ResourceConsuptionException e) {
                    e.printStackTrace();
                }
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
    private void updateEatingInhabitants() throws NoFoodException{
        ResourceRequirement resourceRequirement = new ResourceRequirement(ResourceType.FOOD, Inhabitants.getInstance().getNumberInhabitants());
        try {
            Resource.getInstance().consumeResource(resourceRequirement);
        } catch (ResourceConsuptionException e) {
            throw new NoFoodException("Not enough food for inhabitants.");
        }
        
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
        Building newBuilding = null;
        try {
            newBuilding = BuildingFactory.createBuilding(buildingType,position);
        }catch (BuildingException e){
            e.printStackTrace();
            return false;
        }
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
        try {
            return building.addWorkers(workers);
        } catch (WorkerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove workers from a building
     * @param building
     * @param workers
     * @return true if the workers were removed, false otherwise
     */
    public boolean removeWorkers(Building building, int workers) {
        try {
            return building.removeWorkers(workers);
        } catch (WorkerException e) {
            e.printStackTrace();
            return false;
        }
    }
}