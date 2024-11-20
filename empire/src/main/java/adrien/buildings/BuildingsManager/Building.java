package adrien.buildings.BuildingsManager;

import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;

public abstract class Building {
    private BuildingType type;
    private int maxInhabitants;
    private int maxWorkers;
    private int currentWorkers;
    private int constructionTime;
    private int width;
    private int height;
    private ResourceRequirement[] constructionMaterials;
    private ResourceRequirement[] consumption;
    private ResourceRequirement[] production;

    protected Building(BuildingType type, int maxInhabitants, int maxWorkers,
                       int constructionTime, int width, int height, ResourceRequirement[] constructionMaterials,
                       ResourceRequirement[] consumption, ResourceRequirement[] production) {
        this.type = type;
        this.maxInhabitants = maxInhabitants;
        this.maxWorkers = maxWorkers;
        this.currentWorkers = 0;
        this.constructionTime = constructionTime;
        this.width = width;
        this.height = height;
        this.constructionMaterials = constructionMaterials;
        this.consumption = consumption;
        this.production = production;
    }

    public BuildingType getType() {
        return type;
    }

    public int getMaxInhabitants() {
        return maxInhabitants;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public int getCurrentWorkers() {
        return currentWorkers;
    }

    public void setCurrentWorkers(int currentWorkers) {
        this.currentWorkers = currentWorkers;
    }

    public int getConstructionTime() {
        return constructionTime;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTotalInhabitants() {
        return maxInhabitants; // Ajustez cette logique selon vos besoins
    }

    public void produceResources() {
        for (ResourceRequirement resourceRequirement : production) {
            ResourceRequirement newResourceRequirement = new ResourceRequirement(resourceRequirement.getResourceType(), resourceRequirement.getQuantity() * currentWorkers);
            Resource.addResource(newResourceRequirement);
        }
    }

    public void consumeResources() {
        for(ResourceRequirement resourceRequirement : consumption){
            ResourceRequirement newResourceRequirement = new ResourceRequirement(resourceRequirement.getResourceType(), resourceRequirement.getQuantity() * currentWorkers);
            Resource.consumeResource(newResourceRequirement);
        }
    }

    public void costBuildingResources(){
        for(ResourceRequirement resourceRequirement : constructionMaterials){
            Resource.consumeResource(resourceRequirement);
        }
    }
}
