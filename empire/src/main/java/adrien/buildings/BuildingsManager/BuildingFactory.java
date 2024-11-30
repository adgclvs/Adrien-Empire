package adrien.buildings.BuildingsManager;

import adrien.Position;
import adrien.buildings.*;

public class BuildingFactory {

    public static Building createBuilding(BuildingType type, Position pos) {
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
                throw new IllegalArgumentException("Unknown building type: " + type);
        }
    }
}
