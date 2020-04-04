package towerdefense;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage WINDOW;

    public static void setScene(Scene scene) {
        WINDOW.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        WINDOW = primaryStage;
        WINDOW.show();
        Game game = new Game();
        game.start();
    }
}
