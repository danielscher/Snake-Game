package gui;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL url = getClass().getResource("/snakeGame.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/mainScene.css").toExternalForm());
        stage.show();

        MainController controller = loader.getController();
        controller.initData(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
