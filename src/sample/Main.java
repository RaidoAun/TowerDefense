package sample;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas();
        Map map = new Map(40,20,10,canvas);
        map.initMap();
        for (int i = 0; i < 2; i++) {
            map.genMap();
        }
        map.drawMap();
        root.getChildren().add(canvas);
        canvas.setOnMouseClicked(evt->{
            map.editMap_matrix(1,1,new Block(1,1,new Color(1,0,0,1)));
            map.drawBlock(1,1);
        });
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        //launch(args);
        int[] start = new int[]{9, 0}; // [x, y] ei ole maatriksi kordinaadid!
        int[] end = new int[]{0, 9};
        int[][] seinad = new int[][]{{1, 1}, {9, 1}, {8, 1}, {7, 1}, {6, 1}, {5, 1}, {4, 1}, {3, 1}, {2, 1}, {9, 8}, {8, 8}, {7, 8}, {6, 8}, {5, 8}, {4, 8}, {3, 8}, {2, 8}};
        MapTest testMap = new MapTest(10, 10, seinad, start, end);
        Pathfinder pathfinder = new Pathfinder(testMap.getMap(), start, end);
        pathfinder.printPath();
    }
}
