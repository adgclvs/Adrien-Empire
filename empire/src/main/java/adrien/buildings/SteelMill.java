package adrien.buildings;

import adrien.Position;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class SteelMill extends Building {
    public SteelMill(Position pos) {
        super(pos,BuildingType.STEEL_MILL, 0, 40, 6, 4, 3,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 100),
                  new ResourceRequirement(ResourceType.STONE, 50),
              },
              new ResourceRequirement[]{
                new ResourceRequirement(ResourceType.IRON, 4),
                new ResourceRequirement(ResourceType.COAL, 2),
            },
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.STEEL, 4)
              });
    }
}
