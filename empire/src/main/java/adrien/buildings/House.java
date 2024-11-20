package adrien.buildings;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class House extends Building {

    public House() {
        super(BuildingType.HOUSE, 4, 0, 4, 2, 2,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 2),
                  new ResourceRequirement(ResourceType.STONE, 2)
              },
              null,
              null);
        costBuildingResources();
    }

}
