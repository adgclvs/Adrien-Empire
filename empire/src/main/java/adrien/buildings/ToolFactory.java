package adrien.buildings;

import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class ToolFactory extends Building {
    public ToolFactory() {
        super(BuildingType.TOOL_FACTORY, 0, 12, 8, 4, 3,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 50),
                  new ResourceRequirement(ResourceType.STONE, 50)
              },
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.STEEL, 4),
                  new ResourceRequirement(ResourceType.COAL, 4)
              },
              
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.TOOLS, 4)
              });
        costBuildingResources();
    }
}
