package adrien.buildings.BuildingsManager;

import adrien.buildings.*;
import adrien.exceptions.BuildingException;

public class BuildingFactory {

    public static Building createBuilding(BuildingType type, Position pos) throws BuildingException{
        switch (type) {
            case APARTMENT_BUILDING:
                return new ApartmentBuilding(pos);
            case CEMENT_PLANT:
                return new CementPlant(pos);
            case FARM:
                return new Farm(pos);
            case HOUSE:
                return new House(pos);
            case LUMBER_MILL:
                return new LumberMill(pos);
            case QUARRY:
                return new Quarry(pos);
            case STEEL_MILL:
                return new SteelMill(pos);
            case TOOL_FACTORY:
                return new ToolFactory(pos);
            case WOODEN_CABIN:
                return new WoodenCabin(pos);
            default:
                throw new BuildingException("Building type not found");
        }
    }
}
