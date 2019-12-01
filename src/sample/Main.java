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
        Map map = new Map(80,40,10,canvas);
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
        int[] start = new int[]{20, 20}; // [x, y] ei ole maatriksi kordinaadid!
        int[] end = new int[]{25, 30};
        int[][] seinad = new int[][]{{1, 1}, {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, {7, 1}, {8, 1}, {9, 1}, {1, 8}, {2, 8}, {3, 8}, {4, 8}, {5, 8}, {6, 8}, {7, 8}, {8, 8}, {9, 8}};
        MapTest testMap = new MapTest(10, 10, seinad, start, end);
        Pathfinder pathfinder = new Pathfinder(map.numbriMatrix(map.getMap_matrix()), start, end);
        pathfinder.printPath();

        for (int[] closed : pathfinder.getClosedList()) {
            map.getMap_matrix()[closed[0]][closed[1]].setColor(new Color(1, 1, 0, 1));
            map.drawBlock(closed[0], closed[1]);
        }

        for (int[] xy : pathfinder.getFinalPath()) {
            map.getMap_matrix()[xy[0]][xy[1]].setColor(new Color(0, 1, 0, 1));
            map.drawBlock(xy[0], xy[1]);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
