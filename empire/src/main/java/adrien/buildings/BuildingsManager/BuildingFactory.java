package adrien.buildings.BuildingsManager;

import adrien.buildings.ApartmentBuilding;
import adrien.buildings.CementPlant;
import adrien.buildings.Farm;
import adrien.buildings.House;
import adrien.buildings.LumberMill;
import adrien.buildings.Quarry;
import adrien.buildings.SteelMill;
import adrien.buildings.ToolFactory;
import adrien.buildings.WoodenCabin;

public class BuildingFactory {

    public static Building createBuilding(BuildingType type) {
        switch (type) {
            case APARTMENT_BUILDING:
                return new ApartmentBuilding();
            case CEMENT_PLANT:
                return new CementPlant();
            case FARM:
                return new Farm();
            case HOUSE:
                return new House();
            case LUMBER_MILL:
                return new LumberMill();
            case QUARRY:
                return new Quarry();
            case STEEL_MILL:
                return new SteelMill();
            case TOOL_FACTORY:
                return new ToolFactory();
            case WOODEN_CABIN:
                return new WoodenCabin();
            default:
                throw new IllegalArgumentException("Unknown building type: " + type);
        }
    }
}
