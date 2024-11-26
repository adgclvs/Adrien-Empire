package adrien.buildings.BuildingsManager;

import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;
import adrien.Observable;
import adrien.Position;


public abstract class Building extends Observable{
    private final int tick_in_hour = 24;

    private BuildingType type;

    private int maxInhabitants;
    private int maxWorkers;
    private int currentWorkers;

    private int constructionTime;
    private int constructionTimeRemaining;
    private boolean isOperational;

    private int width;
    private int height;
    
    private Position origin;

    private ResourceRequirement[] constructionMaterials;
    private ResourceRequirement[] consumption;
    private ResourceRequirement[] production;

     /*************************************CONSTRUCTOR***************************************** */

    protected Building(Position position,BuildingType type, int maxInhabitants, int maxWorkers,
                       int constructionTime, int width, int height, ResourceRequirement[] constructionMaterials,
                       ResourceRequirement[] consumption, ResourceRequirement[] production) {
                        super();
        this.type = type;
        this.maxInhabitants = maxInhabitants;
        this.maxWorkers = maxWorkers;
        this.currentWorkers = 0;
        this.constructionTime = tick_in_hour * constructionTime;
        this.constructionTimeRemaining = this.constructionTime;
        this.isOperational = false;
        this.width = width;
        this.height = height;
        this.constructionMaterials = constructionMaterials;
        this.consumption = consumption;
        this.production = production;
        this.origin = position;
    }

     /*************************************GETTER***************************************** */

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
    
    public int getConstructionTime() {
        return constructionTime;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getConstructionTimeRemaining() {
        return constructionTimeRemaining;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public ResourceRequirement[] getConsumption() {
        return consumption;
    }

    public ResourceRequirement[] getProduction() {
        return production;
    }

    public Position getOrigin() {
        return origin;
    }

     /*************************************SETTER***************************************** */

    public void setCurrentWorkers(int currentWorkers) {
        this.currentWorkers = currentWorkers;
    }

    public void decrementConstructionTime() {
        if (this.constructionTimeRemaining > 0) {
            this.constructionTimeRemaining--;
        }
    }

    public void setOperational(boolean _isOperational) {
        isOperational = _isOperational;
    }

     /*************************************WORKERS***************************************** */

    public void addWorkers(int workers) {
        currentWorkers += workers;
    }

    public void removeWorkers(int workers) {
        currentWorkers -= workers;
    }

     /*************************************RESOURCES***************************************** */

    public void produceResources() {
        for (ResourceRequirement resourceRequirement : production) {
            ResourceRequirement newResourceRequirement = new ResourceRequirement(resourceRequirement.getResourceType(), resourceRequirement.getQuantity() * currentWorkers);
            Resource.addResource(newResourceRequirement);
        }
    }

    public boolean consumeResources() {
        boolean hasEnoughResources = true;
    
        for (ResourceRequirement resourceRequirement : consumption) {
            ResourceRequirement newResourceRequirement = new ResourceRequirement(
                resourceRequirement.getResourceType(),
                resourceRequirement.getQuantity() * currentWorkers
            );
    
            if (!Resource.consumeResource(newResourceRequirement)) {
                hasEnoughResources = false;
                break;
            }
        }
    
        // Vérifier si l'état opérationnel a changé
        if (hasEnoughResources != isOperational) {
            isOperational = hasEnoughResources;
            notifyObservers(); // Notifier uniquement si l'état change
        }
    
        return hasEnoughResources;
    }
    

    public void costBuildingResources(){
        for(ResourceRequirement resourceRequirement : constructionMaterials){
            Resource.consumeResource(resourceRequirement);
        }
    }
}
