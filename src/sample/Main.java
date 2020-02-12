package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    private static Canvas canvas = new Canvas();
    private static GraphicsContext gc = canvas.getGraphicsContext2D();
    private static List<Monster> monsters = new ArrayList<>();
    private static List<Block> towers = new ArrayList<>();
    static Map map = new Map(300,150,5, canvas);
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
            for (Block tower : getTowers()) {
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
        map.genOpenBlocks();
        map.generateSpawnpoints();
        map.spawnSpawnpoints();
        for (Spawnpoint spawn : map.getSpawnpoints()) {
            map.drawPath(spawn.getPath());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Canvas getCanvas() {
        return canvas;
    }

    static GraphicsContext getGc() {
        return gc;
    }

    private void drawMonsters(){
        for (int i = getMonsters().size()-1; i >=0 ; i--) {
            if (getMonsters().get(i).getHp()<=0){
               monsters.remove(getMonsters().get(i));
            }
        }
        for (Monster monster:monsters) {
            monster.drawMonster();
        }
    }

    private void shootTowers(){
        for (Block tower:
             getTowers()) {
            tower.shoot(getMonsters());
        }
    }

    private static List<Block> getTowers() {
        return towers;
    }

    private static List<Monster> getMonsters() {
        return monsters;
    }

    private int convertPixelToIndex(double pixel_coords){
        return (int)pixel_coords/map.getSize();
    }

    private void drawTowerRanges(){
        for (Block tower:
                getTowers()) {
            if (tower.getActive()){
                tower.drawRange();
            }
        }
    }

}

