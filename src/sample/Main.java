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
            //System.out.println("tere");
            map.editMap_matrix(1,1,new Block(1,1,new Color(1,0,0,1)));
            map.drawBlock(1,1);
        });
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        //launch(args);
        int[] start = new int[]{0, 0}; // [x, y] ei ole maatriksi kordinaadid!
        int[] end = new int[]{9, 9};
        int[][] seinad = new int[][]{new int[]{1, 1}};
        MapTest testMap = new MapTest(10, 10, seinad, start, end);
        testMap.printMap();
        Pathfinder pathfinder = new Pathfinder(testMap.getMap(), start, end);
        System.out.println(Arrays.deepToString(pathfinder.scanMap().toArray()));
    }
}
