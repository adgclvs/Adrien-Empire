package adrien;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingFactory;
import adrien.buildings.BuildingsManager.BuildingType;

public class GameManager {

    public GameManager(int mapWidth, int mapHeight) {
        // Resource.getInstance();
        // MapManager.getInstance(mapWidth, mapHeight);
        // UseLess if we initialize it in each controller
    }

    public boolean addBuilding(BuildingType buildingType, int x, int y) {
        Building newBuilding = BuildingFactory.createBuilding(buildingType);
        Position position = new Position(x, y);
        if (MapManager.addBuilding(position, newBuilding)) {
            return true;
        }
        return false;
    }

    public boolean removeBuilding( int x, int y) {
        Position position = new Position(x, y);
        if (MapManager.removeBuilding(position)) {
            // Logique pour libérer l'espace sur la carte
            return true;
        }
        return false;
    }

    public void addWorkers(Building building, int workers) {
        building.setCurrentWorkers(building.getCurrentWorkers() + workers);

    }

    public void removeWorkers(Building building, int workers) {
        building.setCurrentWorkers(building.getCurrentWorkers() - workers);
    }

    // public void produceResources() {
    //     for (Building building : buildings) {
    //         for (ResourceRequirement production : building.getProduction()) {
    //             resources.addResource(production);
    //         }
    //     }
    // }

    // public void consumeResources() {
    //     for (Building building : buildings) {
    //         // Logique de consommation des ressources
    //         // Exemple simplifié : chaque habitant consomme 1 unité de nourriture par cycle
    //         for (ResourceRequirement consumption : building.getConsumption()) {
    //             resources.consumeResource(consumption);
    //         }
    //     }
    // }

}

