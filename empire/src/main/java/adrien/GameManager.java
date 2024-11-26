package adrien;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingFactory;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.Resource;

public class GameManager {

    private GameTimer gameTimer;
    private int mapWidth;
    private int mapHeight;

    public GameManager(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        MapManager.getInstance();
        Resource.getInstance();
        Inhabitants.getInstance(0);
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
            //controllerRessources.update();
            System.out.println("Resources updated (24-hour cycle).");
        }
        updateBuildingStatuses();
    }

    private void updateResourceProduction() {
        for (Building building : MapManager.getAllBuildings()) {
            if (building.isOperational() && building.getCurrentWorkers() > 0) {
                building.produceResources();
                System.out.println("Resources produced by: " + building.getType());
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
                    Inhabitants.addInhabitants(building.getMaxInhabitants());
                }
            }
        }
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

    public void addWorkers(Building building, int workers) {
        building.setCurrentWorkers(building.getCurrentWorkers() + workers);
    }

    public void removeWorkers(Building building, int workers) {
        building.setCurrentWorkers(building.getCurrentWorkers() - workers);
    }
}