package adrien.resources;

public class ResourceRequirement {
    private ResourceType resourceType;
    private int quantity;

    public ResourceRequirement(ResourceType resourceType, int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getQuantity() {
        return quantity;
    }
}