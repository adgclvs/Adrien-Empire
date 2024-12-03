package adrien.resources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import adrien.Observable;
import adrien.Observer;
import javafx.scene.image.Image;

public class Resource extends Observable {
    private final Map<String, Image> imageCache = new HashMap<>();
    private static Resource instance;
    private Map<ResourceType, Integer> resources;

     /*************************************CONSTRUCTOR***************************************** */

    /**
     * Constructor for Resource
     */
    private Resource() {
        resources = new HashMap<>();
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

    /**
     * Get the instance of the Resource
     * @return instance
    */
    public static Resource getInstance() {
        if (instance == null) {
            instance = new Resource();
        }
        return instance;
    }

     /*************************************GETTER***************************************** */

    /**
     * Get the amount of a specific resource
     * @param resourceType
     * @return amount
     */
    public int getResource(ResourceType resourceType) {
        return resources.getOrDefault(resourceType, 0);
    }

    /**
     * Get all resources
     * @return resources
     */
    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    /**
     * Check if the city has all the resources required
     * @param resourceRequirements
     * @return boolean
     */
    public boolean haveAllResources(ResourceRequirement[] resourceRequirements) {
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

    /**
     * Get the image of a resource
     * @param resourceType
     * @return image
     */
    public Image getResourceImage(ResourceType resourceType) {
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

    /**
     * Add resources
     * @param resourceRequirement
     */
     public void addResource(ResourceRequirement resourceRequirement) {
        ResourceType resourceType = resourceRequirement.getResourceType();
        int amount = resourceRequirement.getQuantity();
        int currentAmount = resources.getOrDefault(resourceType, 0);
        resources.put(resourceType, currentAmount + amount);
        this.notifyObservers();
    }

    /**
     * Consume resources
     * @param resourceRequirement
     * @return boolean
     */
    public boolean consumeResource(ResourceRequirement resourceRequirement) {
        ResourceType resourceType = resourceRequirement.getResourceType();
        int amount = resourceRequirement.getQuantity();
        System.out.println("Consuming " + amount + " " + resourceType);
        int currentAmount = resources.getOrDefault(resourceType, 0);
        if (currentAmount >= amount) {
            resources.put(resourceType, currentAmount - amount);
            this.notifyObservers();
            return true;
        } else {
            System.out.println("Not enough " + resourceType);
            return false;
        }
    }

}
