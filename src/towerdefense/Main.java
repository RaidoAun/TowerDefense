package towerdefense;

import display.ScenesGroup;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(ScenesGroup.MENU);
        primaryStage.show();
    }
}
