package adrien.buildings;

import adrien.buildings.BuildingsManager.Building;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class WoodenCabin extends Building {
    public WoodenCabin() {
        super("WoodenCabin", 2, 2, 2, 1, 1,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 1)
              },
              null,
              
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 2),
                  new ResourceRequirement(ResourceType.FOOD, 2)
              });
        costBuildingResources();
    }

}
