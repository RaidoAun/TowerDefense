package sample;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    static Canvas canvas = new Canvas();
    static GraphicsContext gc = canvas.getGraphicsContext2D();
    List<Monster> monsters = new ArrayList<>();
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Group root = new Group();
        Scene scene = new Scene(root);
        Map map = new Map(160,80,10);
        map.initMap();
        for (int i = 0; i < 2; i++) {
            map.genMap();
        }
        map.drawMap();
        root.getChildren().add(canvas);
        Monster koll = new Monster(0,100,100);
        koll.drawMonster();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Canvas getCanvas() {
        return canvas;
    }

    public static GraphicsContext getGc() {
        return gc;
    }
    public  void drawMonsters(){
        for (Monster monster:monsters) {
            monster.drawMonster();
        }
    }
}
