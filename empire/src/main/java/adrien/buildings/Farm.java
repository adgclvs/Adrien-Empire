package adrien.buildings;

import adrien.buildings.BuildingsManager.Building;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class Farm extends Building {

    public Farm() {
        super("Cement Plant", 0, 10, 4, 4, 3,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 50),
                  new ResourceRequirement(ResourceType.STONE, 50)
              },
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.STONE, 4),
                  new ResourceRequirement(ResourceType.COAL, 4)
              },
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.CEMENT, 4)
              });
              costBuildingResources();
    }
}