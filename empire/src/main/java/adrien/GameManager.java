package adrien;

import java.util.Scanner;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingFactory;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;

public class GameManager {

    private GameTimer gameTimer;

    /*************************************CONSTRUCTOR***************************************** */

    /**
     * Constructeur de GameManager
     * @param mapWidth
     * @param mapHeight
     */
    public GameManager(int mapWidth, int mapHeight) {
        MapManager.getInstance(mapWidth, mapHeight);
        Resource.getInstance();
        Inhabitants.getInstance(0);
        this.gameTimer = new GameTimer(1000);
    }

    /*************************************GAME***************************************** */

    public void startGame() {
        // Ajouter des actions à exécuter à chaque tick
        gameTimer.addTickListener(this::gameTick);

        // Démarrer le timer
        gameTimer.start();
    }

    public void stopGame() {
        gameTimer.stop();
    }


    public void gameTick() {
        System.out.println("Tick executed.");

        // Gestion des actions spécifiques
        if (gameTimer.getCurrentTick() % 24 == 0 && gameTimer.getCurrentTick() > 0) {
            updateResourceProduction();
            updateResourceConsumption();
            System.out.println("Resources updated (24-hour cycle).");
        }
        updateBuildingStatuses();

    }

    /**
     * Traiter une commande utilisateur
     * @param input
     */
    private void processInput(String input) {
        String[] parts = input.split(" ");
        String command = parts[0].toLowerCase();
    
        try {
            switch (command) {
                case "addbuilding":
                    if (parts.length >= 4) {
                        BuildingType buildingType = BuildingType.valueOf(parts[1].toUpperCase());
                        int x = Integer.parseInt(parts[2]);
                        int y = Integer.parseInt(parts[3]);
                        boolean added = addBuilding(buildingType, x, y);
                        System.out.println(added ? "Building added successfully." : "Failed to add building.");
                    } else {
                        System.out.println("Usage: addbuilding <BuildingType> <x> <y>");
                    }
                    break;
    
                case "removebuilding":
                    if (parts.length >= 3) {
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        boolean removed = removeBuilding(x, y);
                        System.out.println(removed ? "Building removed successfully." : "Failed to remove building.");
                    } else {
                        System.out.println("Usage: removebuilding <x> <y>");
                    }
                    break;
    
                case "addworkers":
                    // Exemple simplifié pour ajouter des workers à un bâtiment
                    // Cela suppose que vous avez une manière d'identifier le bâtiment (par ex. ID ou position)
                    System.out.println("This command needs implementation depending on how you identify buildings.");
                    break;
    
                case "removeworkers":
                    // Exemple simplifié pour retirer des workers d'un bâtiment
                    System.out.println("This command needs implementation depending on how you identify buildings.");
                    break;
    
                case "quit":
                    System.out.println("Stopping the game...");
                    stopGame();
                    break;
    
                default:
                    System.out.println("Unknown command: " + input);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error processing command: " + e.getMessage());
        }
    }

        /*************************************UPDATE***************************************** */

    /**
     * Mettre à jour la production de ressources
     */
    private void updateResourceProduction() {
        for (Building building : MapManager.getAllBuildings()) {
            if (building.isOperational() && building.getCurrentWorkers() > 0) {
                building.produceResources();
                System.out.println("Resources produced by: " + building.getType());
            }
        }
    }
    
    
 

    /*
     * Mettre à jour la consommation de ressources
     */
    private void updateResourceConsumption() {
        for (Building building : MapManager.getAllBuildings()) {
            if (building.isOperational() && building.getCurrentWorkers() > 0) {
                building.consumeResources();
                System.out.println("Resources consumed by: " + building.getType());
            }
        }
    }

    /**
     * Mettre à jour les états des bâtiments
     */
    private void updateBuildingStatuses() {
        for (Building building : MapManager.getAllBuildings()) {
            if (building.getConstructionTimeRemaining() > 0) {
                building.decrementConstructionTime(); // Réduire le temps restant
                if (building.getConstructionTimeRemaining() <= 0) {
                    System.out.println("Building completed: " + building.getType());
                    building.setOperational(true); // Marquer le bâtiment comme opérationnel
                    Inhabitants.addInhabitants(building.getMaxInhabitants());
                }
            }
        }
    }
    
    /*************************************BUILDINGS***************************************** */

    /**
     * Ajouter un bâtiment à la carte
     * @param buildingType
     * @param x
     * @param y
     * @return
     */
    public boolean addBuilding(BuildingType buildingType, int x, int y) {
        Building newBuilding = BuildingFactory.createBuilding(buildingType);
        Position position = new Position(x, y);
        if (MapManager.addBuilding(position, newBuilding)) {
            return true;
        }
        return false;
    }

    /**
     * Supprimer un bâtiment de la carte
     * @param x
     * @param y
     * @return
     */
    public boolean removeBuilding( int x, int y) {
        Position position = new Position(x, y);
        if (MapManager.removeBuilding(position)) {
            // Logique pour libérer l'espace sur la carte
            return true;
        }
        return false;
    }

    /**
     * Ajouter des travailleurs à un bâtiment
     * @param building
     * @param workers
     */
    public void addWorkers(Building building, int workers) {
        building.setCurrentWorkers(building.getCurrentWorkers() + workers);

    }

    /**
     * Retirer des travailleurs d'un bâtiment
     * @param building
     * @param workers
     */
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

