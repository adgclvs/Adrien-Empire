package adrien.buildings;

import adrien.Position;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class CementPlant extends Building {

    public CementPlant(Position pos) {
        super(pos,BuildingType.CEMENT_PLANT, 0, 10, 4, 4, 3,
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
    }
}
