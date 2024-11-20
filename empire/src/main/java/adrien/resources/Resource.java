package adrien.resources;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class Resource {
    private static Resource instance;
    private static Map<ResourceType, Integer> resources;

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

    public static synchronized Resource getInstance() {
        if (instance == null) {
            instance = new Resource();
        }
        return instance;
    }

    public static int getResource(ResourceType resourceType) {
        return resources.getOrDefault(resourceType, 0);
    }

    public static void addResource(ResourceRequirement resourceRequirement) {
        ResourceType resourceType = resourceRequirement.getResourceType();
        int amount = resourceRequirement.getQuantity();
        int currentAmount = resources.getOrDefault(resourceType, 0);
        resources.put(resourceType, currentAmount + amount);
    }

    public static void consumeResource(ResourceRequirement resourceRequirement) {
        ResourceType resourceType = resourceRequirement.getResourceType();
        int amount = resourceRequirement.getQuantity();
        int currentAmount = resources.getOrDefault(resourceType, 0);
        if (currentAmount >= amount) {
            resources.put(resourceType, currentAmount - amount);
        } else {
            System.out.println("Not enough " + resourceType);
        }
    }

    public static Image getResourceImage(ResourceType resourceType) {
        String imagePath = "/adrien/images/resources/" + resourceType.toString().toLowerCase() + ".png";
        return new Image(Resource.class.getResourceAsStream(imagePath));
    }
}
