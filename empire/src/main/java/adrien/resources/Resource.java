package adrien.resources;

import java.util.HashMap;
import java.util.Map;

import adrien.ImageCache;
import adrien.Observable;
import adrien.Exceptions.CustomException;
import adrien.Exceptions.ResourceConsuptionException;
import javafx.scene.image.Image;

public class Resource extends Observable {
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
    public static Resource getInstance(){
        if (instance == null) {
            instance = new Resource();
        }
        return instance;
    }

    /**
     * Set the instance of the Resource for testing purposes
     * @param newInstance
     */
    public static void setInstance(Resource newInstance) {
        instance = newInstance;
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
     * Get the image of a resource
     * @param resourceType
     * @return image
     */
    public Image getResourceImage(ResourceType resourceType) {

        try {
            Image image = ImageCache.getImage("/adrien/images/resources/" + resourceType.toString().toLowerCase() + ".png");
            return image;
        } catch (Exception e) {
            System.err.println("Failed to load image for resource: " + resourceType.toString());
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
    public boolean consumeResource(ResourceRequirement resourceRequirement) throws ResourceConsuptionException{
        ResourceType resourceType = resourceRequirement.getResourceType();
        int amount = resourceRequirement.getQuantity();
        int currentAmount = resources.getOrDefault(resourceType, 0);
        if (currentAmount >= amount) {
            resources.put(resourceType, currentAmount - amount);
            this.notifyObservers();
            return true;
        } else {
            throw new ResourceConsuptionException("Not enough resources: " + resourceType.toString());
        }
    }

}
