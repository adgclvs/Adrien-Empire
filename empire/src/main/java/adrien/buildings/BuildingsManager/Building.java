package adrien.buildings.BuildingsManager;

import adrien.resources.*;
import adrien.exceptions.ResourceConsuptionException;
import adrien.exceptions.ResourceProductionException;
import adrien.exceptions.WorkerException;
import adrien.game.Inhabitants;


public abstract class Building{
    private static final int TICK_IN_A_DAY = 24;
    private static final int INITIAL_CURRENT_WORKERS = 0;

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
        this.currentWorkers = INITIAL_CURRENT_WORKERS;
        this.constructionTime = TICK_IN_A_DAY * constructionTime;
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
    public boolean addWorkers(int workers) throws WorkerException {
        if( !isOperational){
            throw new WorkerException("Building is not operational.");
        }
        if (currentWorkers + workers > maxWorkers) {
            throw new WorkerException("Cannot add more workers than the maximum.");
        }
        if (Inhabitants.getInstance().addWorkers(workers)) {
            currentWorkers += workers;
            setProducing(true);
        } else {
            throw new WorkerException("Failed to add workers to Inhabitants.");
        }
    return true;
}

    /**
     * Remove workers from the building
     * @param workers
     * @return true if the workers were removed, false otherwise
     */
    public boolean removeWorkers(int workers) throws WorkerException {
        if(!isOperational){
            throw new WorkerException("Building is not operational.");
        }
        if(currentWorkers - workers < 0){
            throw new WorkerException("Cannot remove more workers than the current number.");
        }
        if(Inhabitants.getInstance().removeWorkers(workers)){
            currentWorkers -= workers;
            setProducing(false);
        }else{
            throw new WorkerException("Failed to remove workers from Inhabitants.");
        }
        return true;
    }

     /*************************************RESOURCES***************************************** */

    /**
     * Produce resources
     */
    public void produceResources() throws ResourceProductionException {
        if(production == null){
            throw new ResourceProductionException("Building does not produce resources.");
        }
        for (ResourceRequirement resourceRequirement : production) {
            ResourceRequirement newResourceRequirement = new ResourceRequirement(resourceRequirement.getResourceType(), resourceRequirement.getQuantity() * currentWorkers);
            Resource.getInstance().addResource(newResourceRequirement);
        }
    }

    /**
     * Consume resources
     */
    public void consumeResources() throws ResourceConsuptionException {
        // Check si le batiment consomme des ressources
        if(consumption == null){
            throw new ResourceConsuptionException("Building does not consume resources.");
        }
        // Consomme les ressources
        for (ResourceRequirement resourceRequirement : consumption) {
            ResourceRequirement newResourceRequirement = new ResourceRequirement(
                resourceRequirement.getResourceType(),
                resourceRequirement.getQuantity() * currentWorkers
            );
            try{
                Resource.getInstance().consumeResource(newResourceRequirement);
            }catch(ResourceConsuptionException e){
                setProducing(false);
                throw new ResourceConsuptionException("Not enough resources to consume.");
            }
        }
    }
    

    /**
     * Check if the building has all the resources to be built
     * @return true if the building has all the resources, false otherwise
     */
    public boolean costBuildingResources() {
        Resource instance = Resource.getInstance();
        for(ResourceRequirement resourceRequirement : constructionMaterials){
            try{
                instance.consumeResource(resourceRequirement);
            }catch(ResourceConsuptionException e){
                return false;
            }
        }
        return true;
    }
}
