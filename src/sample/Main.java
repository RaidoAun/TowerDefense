package sample;
import javafx.animation.AnimationTimer;
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

import java.util.Arrays;

public class Main extends Application {
    private static Canvas canvas = new Canvas();
    private static GraphicsContext gc = canvas.getGraphicsContext2D();
    private static List<Monster> monsters = new ArrayList<>();
    private static List<Block> towers = new ArrayList<>();
    static Map map = new Map(160,80,10);
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Group root = new Group();
        Scene scene = new Scene(root);

        map.initMap();
        for (int i = 0; i < 2; i++) {
            map.genMap();
        }
        map.drawMap();
        root.getChildren().add(canvas);
        monsters.add(new Monster(0,100,100));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        canvas.setOnMouseClicked(e -> {
            int i = convertPixelToIndex((e.getX()));
            int j = convertPixelToIndex(e.getY());
            Block eventBlock = map.getMap_matrix()[i][j];
            for (Block tower:
                    getTowers()) {
                tower.setActive(false);
            }
            if (eventBlock.getId()<2){
                eventBlock.makeTower(10,i*map.getSize()+map.getSize()/2,j*map.getSize()+map.getSize()/2);
                //map.drawBlock(convertPixelToIndex((e.getX())),convertPixelToIndex(e.getY()));
                getTowers().add(eventBlock);
                eventBlock.setActive(true);
            }
            else if (eventBlock.getId()>=10){
                eventBlock.setActive(true);
            }

        });
        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                map.drawMap();
                drawTowerRanges();
                drawMonsters();
                shootTowers();
            }
        }.start();
        primaryStage.show();
        int[] start = new int[]{10, 5}; // [x, y] ei ole maatriksi kordinaadid!
        int[] end = new int[]{100, 20};
        int[][] seinad = new int[][]{{1, 1}, {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, {7, 1}, {8, 1}, {9, 1}, {1, 8}, {2, 8}, {3, 8}, {4, 8}, {5, 8}, {6, 8}, {7, 8}, {8, 8}, {9, 8}};
        MapTest testMap = new MapTest(10, 10, seinad, start, end);
        Pathfinder pathfinder = new Pathfinder(/*map.numbriMatrix(map.getMap_matrix())*/ /*map.numbriMatrix(map.getMap_matrix())*/ map.getFlippedMap(), start, end);
        //System.out.println(Arrays.deepToString(pathfinder.getClosedList().toArray()));
        //System.out.println(Arrays.deepToString(pathfinder.getFinalPath()));
        pathfinder.printMap();
        //System.out.println(Arrays.deepToString(pathfinder.getFinalPath()));
        //System.out.println(Arrays.deepToString(pathfinder.getClosedList()));

        /*for (int[] closed : pathfinder.getClosedList()) {
            map.getMap_matrix()[closed[0]][closed[1]].setColor(new Color(1, 1, 0, 1));
            map.drawBlock(closed[0], closed[1]);
        }

        for (int[] xy : pathfinder.getFinalPath()) {
            map.getMap_matrix()[xy[0]][xy[1]].setColor(new Color(0, 1, 0, 1));
            map.drawBlock(xy[0], xy[1]);
        }*/
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
    public void drawMonsters(){
        for (int i = getMonsters().size()-1; i >=0 ; i--) {
            if (getMonsters().get(i).getHp()<=0){
               monsters.remove(getMonsters().get(i));
            }
        }
        for (Monster monster:monsters) {
            monster.drawMonster();
        }
    }
    public void shootTowers(){
        for (Block tower:
             getTowers()) {
            tower.shoot(getMonsters());
        }
    }

    public static List<Block> getTowers() {
        return towers;
    }

    public static List<Monster> getMonsters() {
        return monsters;
    }
    public int convertPixelToIndex (double pixel_coords){
        int index = (int)pixel_coords/map.getSize();
        return index;
    }
    public void drawTowerRanges(){
        for (Block tower:
                getTowers()) {
            if (tower.getActive()){
                tower.drawRange();
            }
        }
    }
}

