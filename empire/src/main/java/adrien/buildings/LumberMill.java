package adrien.buildings;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class LumberMill extends Building {
    public LumberMill() {
        super(BuildingType.LUMBER_MILL, 0, 10, 4, 3, 3,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 50),
                  new ResourceRequirement(ResourceType.STONE, 50)
              },
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 4)
              },
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.LUMBER, 4)
              });
        costBuildingResources();
    }
}
