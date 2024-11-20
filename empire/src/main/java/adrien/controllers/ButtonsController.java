package adrien.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ButtonsController {
    private MainViewController mainViewController;
    private boolean isSelectingBuildingLocation;

    @FXML
    private Button addBuildingButton;

    // @FXML
    // private Button removeBuildingButton;
    
    public boolean isSelectingBuildingLocation() {
        return isSelectingBuildingLocation;
    }

    public void setSelectingBuildingLocation(boolean selectingBuildingLocation){
        isSelectingBuildingLocation = selectingBuildingLocation;
    }

    @FXML
    public void handleAddBuildingButton() {
        isSelectingBuildingLocation = !isSelectingBuildingLocation;
        updateButtonStyle();
    }

    @FXML
    public void handleRemoveBuildingButton() {
        // TODO
    }

    public void updateButtonStyle() {
        if (isSelectingBuildingLocation) {
            addBuildingButton.getStyleClass().remove("button-normal");
            addBuildingButton.getStyleClass().add("button-selected");
        } else {
            addBuildingButton.getStyleClass().remove("button-selected");
            addBuildingButton.getStyleClass().add("button-normal");
        }
    }
}
