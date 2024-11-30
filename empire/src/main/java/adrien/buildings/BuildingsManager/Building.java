package adrien.buildings.BuildingsManager;

import adrien.resources.Resource;
import adrien.resources.ResourceRequirement;
import adrien.Inhabitants;
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
    private boolean isProducing;

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

    public boolean isProducing() {
        return isProducing;
    }

    public ResourceRequirement[] getConsumption() {
        return consumption;
    }

    public ResourceRequirement[] getProduction() {
        return production;
    }

    public ResourceRequirement[] getConstructionMaterials() {
        return constructionMaterials;
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

    public void setOperational(boolean isOperational) {
        this.isOperational = isOperational;
        notifyObservers();
    }

    public void setProducing(boolean isProducing) {
        this.isProducing = isProducing;
    }

     /*************************************WORKERS***************************************** */

    public boolean addWorkers(int workers) {
        if (!isOperational || currentWorkers + workers > maxWorkers) {
            return false;
        }
        if(Inhabitants.addWorkers(workers)){
            currentWorkers += workers;
            setProducing(true);
        }else{
            return false;
        }

        return true;
    }

    public boolean removeWorkers(int workers) {
        if(!isOperational || currentWorkers - workers < 0){
            return false;
        }
        if(Inhabitants.removeWorkers(workers)){
            currentWorkers -= workers;
            setProducing(false);
        }else{
            return false;
        }
        return true;
    }

     /*************************************RESOURCES***************************************** */

    public void produceResources() {
        if(production == null){
            return;
        }
        for (ResourceRequirement resourceRequirement : production) {
            ResourceRequirement newResourceRequirement = new ResourceRequirement(resourceRequirement.getResourceType(), resourceRequirement.getQuantity() * currentWorkers);
            Resource.addResource(newResourceRequirement);
        }
    }

    public void consumeResources() {
        // Check si le batiment consomme des ressources
        if(consumption == null){
            return;
        }
        // Check si on a assez de ressources pour consommer
        if(!Resource.haveAllResources(consumption)){
            System.out.println("Not enough resources to consume");
            setProducing(false);
            notifyObservers();
            return;
        }
        // Consomme les ressources
        for (ResourceRequirement resourceRequirement : consumption) {
            ResourceRequirement newResourceRequirement = new ResourceRequirement(
                resourceRequirement.getResourceType(),
                resourceRequirement.getQuantity() * currentWorkers
            );
    
            Resource.consumeResource(newResourceRequirement);
        }
    }
    

    public boolean costBuildingResources(){
        Resource instance = Resource.getInstance();
        for(ResourceRequirement resourceRequirement : constructionMaterials){
            if (!Resource.consumeResource(resourceRequirement)) {
                return false;
            }
        }
        return true;
    }
}
