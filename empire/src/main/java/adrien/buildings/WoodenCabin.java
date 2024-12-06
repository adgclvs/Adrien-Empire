package adrien.buildings;

import adrien.Position;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class WoodenCabin extends Building {
    public WoodenCabin(Position pos) {
        super(pos,BuildingType.WOODEN_CABIN, 2, 2, 2, 2, 2,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 1)
              },
              null,
              
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 2),
                  new ResourceRequirement(ResourceType.FOOD, 2)
              });
    }

}
