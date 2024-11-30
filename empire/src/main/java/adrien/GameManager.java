package adrien;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingFactory;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class GameManager extends Observable{

    private GameTimer gameTimer;
    private int mapWidth;
    private int mapHeight;

    public GameManager(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        MapManager.getInstance();
        Resource.getInstance();
        Inhabitants.getInstance();
        this.gameTimer = new GameTimer(1000);
    }


    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void startGame() {
        gameTimer.addTickListener(this::gameTick);
        gameTimer.start();
    }

    public void stopGame() {
        gameTimer.stop();
    }

    public void gameTick() {
        System.out.println("Tick executed.");
        if (gameTimer.getCurrentTick() % 24 == 0 && gameTimer.getCurrentTick() > 0) {
            updateResourceProduction();
            updateResourceConsumption();
            updateEatingInhabitants();
            notifyObservers();
            System.out.println("Resources updated (24-hour cycle).");
        }
        Resource.getInstance().notifyObserversAtEndOfTick();
        updateBuildingStatuses();
    }

    private void updateResourceProduction() {
        for (Building building : MapManager.getAllBuildings()) {
            if(building.getCurrentWorkers() > 0){
                if (building.isProducing()) {
                    building.produceResources();
                    System.out.println("Resources produced by: " + building.getType());
                }
                building.setProducing(true);
            }
        }
    }

    private void updateResourceConsumption() {
        for (Building building : MapManager.getAllBuildings()) {
            if (building.isOperational() && building.getCurrentWorkers() > 0) {
                building.consumeResources();
                System.out.println("Resources consumed by: " + building.getType());
            }
        }
    }

    private void updateBuildingStatuses() {
        for (Building building : MapManager.getAllBuildings()) {
            if (building.getConstructionTimeRemaining() > 0) {
                building.decrementConstructionTime();
                if (building.getConstructionTimeRemaining() <= 0) {
                    System.out.println("Building completed: " + building.getType());
                    building.setOperational(true);
                    MapManager.addInitialInhabitant(building);
                }
            }
        }
    }

    private void updateEatingInhabitants() {
        ResourceRequirement resourceRequirement = new ResourceRequirement(ResourceType.FOOD, Inhabitants.getNumberInhabitants());
        Resource.consumeResource(resourceRequirement);
        
    }

    public boolean addBuilding(BuildingType buildingType, int x, int y) {
        Position position = new Position(x, y);
        Building newBuilding = BuildingFactory.createBuilding(buildingType,position);
        return MapManager.addBuilding(position, newBuilding);
    }

    public boolean removeBuilding(int x, int y) {
        Position position = new Position(x, y);
        return MapManager.removeBuilding(position);
    }

    public boolean addWorkers(Building building, int workers) {
       return building.addWorkers(workers);
    }

    public boolean removeWorkers(Building building, int workers) {
        return building.removeWorkers(workers);
    }
}