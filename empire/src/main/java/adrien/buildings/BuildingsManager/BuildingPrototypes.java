package adrien.buildings.BuildingsManager;

import adrien.Position;
import adrien.Exceptions.BuildingException;
import adrien.buildings.ApartmentBuilding;
import adrien.buildings.CementPlant;
import adrien.buildings.Farm;
import adrien.buildings.House;
import adrien.buildings.LumberMill;
import adrien.buildings.Quarry;
import adrien.buildings.SteelMill;
import adrien.buildings.ToolFactory;
import adrien.buildings.WoodenCabin;

public class BuildingPrototypes {

    public static final Building APARTMENT_BUILDING = new ApartmentBuilding(new Position(0, 0));
    public static final Building CEMENT_PLANT = new CementPlant(new Position(0, 0));
    public static final Building FARM = new Farm(new Position(0, 0));
    public static final Building HOUSE = new House(new Position(0, 0));
    public static final Building LUMBER_MILL = new LumberMill(new Position(0, 0));
    public static final Building QUARRY = new Quarry(new Position(0, 0));
    public static final Building STEEL_MILL = new SteelMill(new Position(0, 0));
    public static final Building TOOL_FACTORY = new ToolFactory(new Position(0, 0));
    public static final Building WOODEN_CABIN = new WoodenCabin(new Position(0, 0));

    public static Building getPrototype(BuildingType type) throws BuildingException{
        switch (type) {
            case APARTMENT_BUILDING:
                return APARTMENT_BUILDING;
            case CEMENT_PLANT:
                return CEMENT_PLANT;
            case FARM:
                return FARM;
            case HOUSE:
                return HOUSE;
            case LUMBER_MILL:
                return LUMBER_MILL;
            case QUARRY:
                return QUARRY;
            case STEEL_MILL:
                return STEEL_MILL;
            case TOOL_FACTORY:
                return TOOL_FACTORY;
            case WOODEN_CABIN:
                return WOODEN_CABIN;
            default:
                throw new BuildingException("Building type not found");   
        }
    }
}