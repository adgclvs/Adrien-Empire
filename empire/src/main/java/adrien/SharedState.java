package adrien;

import adrien.buildings.BuildingsManager.BuildingType;

public class SharedState {
    private static BuildingType selectedBuildingType;

    public static BuildingType getSelectedBuildingType() {
        return selectedBuildingType;
    }

    public static void setSelectedBuildingType(BuildingType type) {
        selectedBuildingType = type;
    }
}