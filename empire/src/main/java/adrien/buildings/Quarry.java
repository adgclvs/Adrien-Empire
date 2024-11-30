package adrien.buildings;

import adrien.Position;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class Quarry extends Building {
    public Quarry(Position pos) {
        super(pos,BuildingType.QUARRY, 2, 30, 2, 2, 2,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 50)
              },
              null,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.STONE, 4),
                  new ResourceRequirement(ResourceType.IRON, 4),
                  new ResourceRequirement(ResourceType.COAL, 4)
              });
        
    }
}
