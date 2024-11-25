package adrien;

import adrien.controllers.BuildingsController;
import adrien.controllers.MainController;
import adrien.controllers.MapController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/adrien/views/MainView.fxml"));
        Parent root = mainLoader.load();

        // Charger les contrôleurs des fichiers FXML
       
        

        primaryStage.setTitle("Adrien Of Empire");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
