package adrien.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adrien.Observer;
import javafx.scene.image.Image;

public class Resource {
    private static final Map<String, Image> imageCache = new HashMap<>();
    private static Resource instance;
    private static Map<ResourceType, Integer> resources;
    private List<Observer> observers;

     /*************************************CONSTRUCTOR***************************************** */

    private Resource() {
        resources = new HashMap<>();
        observers = new ArrayList<>();
        resources.put(ResourceType.FOOD, 100);
        resources.put(ResourceType.WOOD, 50);
        resources.put(ResourceType.STONE, 30);
        resources.put(ResourceType.COAL, 0);
        resources.put(ResourceType.IRON, 0);
        resources.put(ResourceType.STEEL, 0);
        resources.put(ResourceType.CEMENT, 0);
        resources.put(ResourceType.LUMBER, 0);
        resources.put(ResourceType.TOOLS, 0);
    }

     /*************************************INSTANCE***************************************** */

    public static synchronized Resource getInstance() {
        if (instance == null) {
            instance = new Resource();
        }
        return instance;
    }

     /*************************************GETTER***************************************** */

    public static int getResource(ResourceType resourceType) {
        return resources.getOrDefault(resourceType, 0);
    }

    public static Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public static boolean haveAllResources(ResourceRequirement[] resourceRequirements) {
        if (resourceRequirements == null) {
            System.out.println("Resource requirements are not initialized.");
            return false;
        }
        for (ResourceRequirement resourceRequirement : resourceRequirements) {
            ResourceType resourceType = resourceRequirement.getResourceType();
            int amount = resourceRequirement.getQuantity();
            int currentAmount = resources.getOrDefault(resourceType, 0);
            if (currentAmount < amount) {
                return false;
            }
        }
        return true;
    }

    public static Image getResourceImage(ResourceType resourceType) {
        String resourceTypeString = resourceType.toString();
        if (imageCache.containsKey(resourceTypeString)) {
            return imageCache.get(resourceTypeString);
        }

        // Charger l'image uniquement si elle n'est pas déjà en cache
        try {
            Image image = new Image(Resource.class.getResourceAsStream("/adrien/images/resources/" + resourceTypeString.toLowerCase() + ".png"));
            imageCache.put(resourceTypeString, image);
            return image;
        } catch (Exception e) {
            System.err.println("Failed to load image for resource: " + resourceTypeString);
            return null;
        }
    }

     /*************************************RESOURCES***************************************** */

     public static void addResource(ResourceRequirement resourceRequirement) {
        ResourceType resourceType = resourceRequirement.getResourceType();
        int amount = resourceRequirement.getQuantity();
        int currentAmount = resources.getOrDefault(resourceType, 0);
        resources.put(resourceType, currentAmount + amount);
        getInstance().notifyObservers();
    }

    public static boolean consumeResource(ResourceRequirement resourceRequirement) {
        ResourceType resourceType = resourceRequirement.getResourceType();
        int amount = resourceRequirement.getQuantity();
        System.out.println("Consuming " + amount + " " + resourceType);
        int currentAmount = resources.getOrDefault(resourceType, 0);
        if (currentAmount >= amount) {
            resources.put(resourceType, currentAmount - amount);
            getInstance().notifyObservers();
            return true;
        } else {
            System.out.println("Not enough " + resourceType);
            return false;
        }
    }

     /*************************************OBSERVER***************************************** */

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
