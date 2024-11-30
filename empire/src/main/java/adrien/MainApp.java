package adrien;

import adrien.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/adrien/views/MainView.fxml"));
        Parent root = mainLoader.load();

        MainController mainController = mainLoader.getController();
        mainController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Adrien Of Empire");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
