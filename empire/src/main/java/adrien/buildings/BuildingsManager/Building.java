package adrien.buildings.BuildingsManager;

import adrien.resources.*;


import adrien.Inhabitants;
import adrien.Observable;
import adrien.Position;


public abstract class Building{
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

    /**
     * @return the type
     */
    public BuildingType getType() {
        return type;
    }

    /**
     * @return the maxInhabitants
     */
    public int getMaxInhabitants() {
        return maxInhabitants;
    }

    /**
     * @return the maxWorkers
     */
    public int getMaxWorkers() {
        return maxWorkers;
    }

    /**
     * @return the currentWorkers
     */
    public int getCurrentWorkers() {
        return currentWorkers;
    }
    
    /**
     * 
     * @return the construction time
     */
    public int getConstructionTime() {
        return constructionTime;
    }

    /**
     * 
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * 
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * 
     * @return the construction time remaining
     */
    public int getConstructionTimeRemaining() {
        return constructionTimeRemaining;
    }

    /**
     * 
     * @return the operational status
     */
    public boolean isOperational() {
        return isOperational;
    }

    /**
     * 
     * @return the producing status
     */
    public boolean isProducing() {
        return isProducing;
    }

    /**
     * 
     * @return the consumption
     */
    public ResourceRequirement[] getConsumption() {
        return consumption;
    }

    /**
     * 
     * @return the production
     */
    public ResourceRequirement[] getProduction() {
        return production;
    }

    /**
     * 
     * @return the construction materials
     */
    public ResourceRequirement[] getConstructionMaterials() {
        return constructionMaterials;
    }

    /**
     * 
     * @return the origin
     */
    public Position getOrigin() {
        return origin;
    }

     /*************************************SETTER***************************************** */

    /**
     * @param currentWorkers the currentWorkers to set
     */
    public void setCurrentWorkers(int currentWorkers) {
        this.currentWorkers = currentWorkers;
    }

    /**
     * @param constructionTimeRemaining the constructionTimeRemaining to set
     */
    public void decrementConstructionTime() {
        if (this.constructionTimeRemaining > 0) {
            this.constructionTimeRemaining--;
        }
    }

    /**
     * @param isOperational the isOperational to set
     */
    public void setOperational(boolean isOperational) {
        this.isOperational = isOperational;
    }

    /**
     * @param isProducing the isProducing to set
     */
    public void setProducing(boolean isProducing) {
        this.isProducing = isProducing;
    }

     /*************************************WORKERS***************************************** */

    /**
     * Add workers to the building
     * @param workers
     * @return true if the workers were added, false otherwise
     */
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

    /**
     * Remove workers from the building
     * @param workers
     * @return true if the workers were removed, false otherwise
     */
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

    /**
     * Produce resources
     */
    public void produceResources() {
        if(production == null){
            return;
        }
        for (ResourceRequirement resourceRequirement : production) {
            ResourceRequirement newResourceRequirement = new ResourceRequirement(resourceRequirement.getResourceType(), resourceRequirement.getQuantity() * currentWorkers);
            Resource.getInstance().addResource(newResourceRequirement);
        }
    }

    /**
     * Consume resources
     */
    public void consumeResources() {
        // Check si le batiment consomme des ressources
        if(consumption == null){
            return;
        }
        // Check si on a assez de ressources pour consommer
        if(!Resource.getInstance().haveAllResources(consumption)){
            System.out.println("Not enough resources to consume");
            setProducing(false);
            return;
        }
        // Consomme les ressources
        for (ResourceRequirement resourceRequirement : consumption) {
            ResourceRequirement newResourceRequirement = new ResourceRequirement(
                resourceRequirement.getResourceType(),
                resourceRequirement.getQuantity() * currentWorkers
            );
    
            Resource.getInstance().consumeResource(newResourceRequirement);
        }
    }
    

    /**
     * Check if the building has all the resources to be built
     * @return true if the building has all the resources, false otherwise
     */
    public boolean costBuildingResources(){
        Resource instance = Resource.getInstance();
        for(ResourceRequirement resourceRequirement : constructionMaterials){
            if (!instance.consumeResource(resourceRequirement)) {
                return false;
            }
        }
        return true;
    }
}
