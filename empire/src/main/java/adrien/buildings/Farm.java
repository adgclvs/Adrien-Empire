package adrien.buildings;

import adrien.Position;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class Farm extends Building {

    public Farm(Position pos) {
        super(pos,BuildingType.FARM, 5, 3, 1, 3, 3,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 5),
                  new ResourceRequirement(ResourceType.STONE, 5)
              },
              null,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.FOOD, 10)
              });
    }
}
