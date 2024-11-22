package adrien;

import adrien.buildings.BuildingsManager.BuildingType;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager(10, 10); // Initialiser avec une carte de 10x10
        gameManager.startGame(); // Démarrer le jeu

        // Ajouter une WoodenCabin
        System.out.println("Adding a wooden cabin at (5, 5)...");
        gameManager.addBuilding(BuildingType.WOODEN_CABIN, 5, 5);
        
        try {
            Thread.sleep(5000); // Attendre 5 secondes
            gameManager.addBuilding(BuildingType.HOUSE, 0, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}