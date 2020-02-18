package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application {
    private static Canvas canvas = new Canvas();
    private static GraphicsContext gc = canvas.getGraphicsContext2D();
    static Map map = new Map(150,80,10, canvas);

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Group root = new Group();
        Scene scene = new Scene(root);

        AnimationTimer animate = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                long startNanoTime = System.nanoTime();
                map.drawMap();
                drawTowerRanges();
                for (Spawnpoint spawn : map.getSpawnpoints()) {
                    spawn.moveMonsters();
                    spawn.drawMonsters();
                    spawn.shootTowers();
                }
                double t = (currentNanoTime - startNanoTime) / 1000000000.0*1000;
                //System.out.println(t);
            }
        };

        map.initMap();
        for (int i = 0; i < 2; i++) {
            map.genMap();
        }
        map.genFlippedMap();
        //Genereerib nii palju spawnpointe, kui võimalik on.
        map.genOpenBlocks();
        map.generateSpawnpoints(5, 40);
        map.spawnSpawnpoints();
        map.drawMap();

        root.getChildren().add(canvas);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        System.out.println("Vali nexuse asukoht kaardil!");
        AtomicBoolean isNexus = new AtomicBoolean(false);

        canvas.setOnMouseClicked(e -> {
            if (!isNexus.get()) {
                int x = convertPixelToIndex(e.getX());
                int y = convertPixelToIndex(e.getY());
                map.setNexusxy(new int[]{x, y});
                map.getSpawnpoints().get(0).genPath(500);
                if (map.getSpawnpoints().get(0).getPath().length == 0) {
                    System.out.println("Valitud nexuse asukoht ei sobi. Proovi uuesti.");
                } else {
                    map.genNexus();
                    map.genPathstoNexus(500);
                    for (Spawnpoint spawn: map.getSpawnpoints()) {
                        map.drawPath(spawn.getPath());
                    }
                    isNexus.set(true);
                    System.out.println("Nexuse asukoht valitud.");
                    animate.start();
                }
            } else {
                int i = convertPixelToIndex((e.getX()));
                int j = convertPixelToIndex(e.getY());
                Block eventBlock = map.getMap_matrix()[i][j];
                System.out.println(eventBlock.getId());

                //Spawnpoindid, mille teele jääb tower ette.
                List<Spawnpoint> updatableSpawns = map.pathsContain(new int[]{i, j});
                //System.out.println(updatableSpawns.size());
                boolean towerPossible = true;
                //Ajutise seina loomine.
                Block oldBlock = map.getBlock(i, j);
                map.editMap_matrix(i, j, new Block(1, 0, new Color(0, 0, 0, 1)));
                //Uuenda spawnpointide path.
                System.out.println(updatableSpawns.size());
                for (Spawnpoint spawn : updatableSpawns) {
                    int[][] oldPath = spawn.getPath();
                    spawn.genPath(500);
                    if (spawn.getPath().length == 0) {
                        towerPossible = false;
                        spawn.setPath(oldPath);
                        break;
                    } else {
                        System.out.println("Uus tee!");
                        //map.deletePath(oldPath);
                        //map.drawPath(spawn.getPath());
                    }
                }

                map.editMap_matrix(i, j, oldBlock);

                if (towerPossible) {
                    for (Block tower : map.getTowers()) {
                        tower.setActive(false);
                    }
                    if (eventBlock.getId() == 0 || eventBlock.getId() == 9){
                        //map.editMap_matrix(i, j, new Block(10, 0, new Color(0, 0, 0, 1)));
                        eventBlock.makeTower(10,i*map.getSize()+map.getSize()/2,j*map.getSize()+map.getSize()/2);
                        map.editMap_matrix(i, j, eventBlock);
                        //map.drawBlock(convertPixelToIndex((e.getX())),convertPixelToIndex(e.getY()));
                        map.getTowers().add(eventBlock);
                        eventBlock.setActive(true);
                    }
                    else if (eventBlock.getId()>=10){
                        eventBlock.setActive(true);
                    }
                    System.out.println(eventBlock.getId());
                    System.out.println("----------------------");
                } else {
                    System.out.println("Towerit pole võimalik maha panna. Proovi uuesti.");
                }

            }
        });

        primaryStage.show();
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

    private int convertPixelToIndex(double pixel_coords){
        return (int)pixel_coords/map.getSize();
    }

    private void drawTowerRanges(){
        for (Block tower: map.getTowers()) {
            if (tower.getActive()){
                tower.drawRange();
            }
        }
    }

}

