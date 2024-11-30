package adrien.buildings;

import adrien.Position;
import adrien.buildings.BuildingsManager.Building;
import adrien.buildings.BuildingsManager.BuildingType;
import adrien.resources.ResourceRequirement;
import adrien.resources.ResourceType;

public class ApartmentBuilding extends Building {

    public ApartmentBuilding(Position pos) {
        super(pos,BuildingType.APARTMENT_BUILDING, 60, 0, 6, 3, 2,
              new ResourceRequirement[]{
                  new ResourceRequirement(ResourceType.WOOD, 50),
                  new ResourceRequirement(ResourceType.STONE, 50)
              },
              null,
              null);
    }

}
