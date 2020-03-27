package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private static Canvas canvas = new Canvas();
    private static GraphicsContext gc = canvas.getGraphicsContext2D();
    private static Map map = new Map(160,85, canvas);
    private static Scene menu_scene;
    private static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        VBox menu_button_box = new VBox();
        menu_button_box.setSpacing(10);
        menu_scene = new Scene(menu_button_box,500,250);
        Button play_btn = new Button("Play");
        Button settings_btn = new Button("Settings");

        menu_button_box.getChildren().addAll(play_btn,settings_btn);
        menu_button_box.setAlignment(Pos.CENTER);

        window.setTitle("TowerDefense");
        window.setScene(menu_scene);
        window.show();

        play_btn.setOnAction(event -> {

            window.setScene(Game.getGameScene());
            window.setMaximized(true);
            Game.generateGame();
            PopUp.createPopup("Vali nexuse asukoht kaardil!\nMäng algab pärast nexuse maha panekut!", true);

            canvas.setOnMouseClicked(e -> {
                if (Game.chooseNexus((int)e.getX(),(int) e.getY())) Game.startRounds();
            });

        });

        settings_btn.setOnAction(e -> {

            window.setTitle("Settings");
            window.setScene(Settings.settingsScene());

        });

    }

    public static void switchToMenu() {
        window.setTitle("TowerDefense");
        window.setScene(menu_scene);
    }

    public static Canvas getCanvas() {
        return canvas;
    }

    public static Map getMap() {
        return map;
    }

    public static GraphicsContext getGc() {
        return gc;
    }

}

