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
        int[] start = new int[]{5, 5}; // [x, y] ei ole maatriksi kordinaadid!
        int[] end = new int[]{10, 15};
        int[][] seinad = new int[][]{};
        MapTest testMap = new MapTest(5, 5, seinad, start, end);
        Pathfinder pathfinder = new Pathfinder(map.numbriMatrix(map.getMap_matrix()), start, end);
        pathfinder.printPath();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
