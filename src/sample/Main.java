package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

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
        map.generateSpawnpoints(6, 30);
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
                map.genPathstoNexus(500);
                map.drawMap();
                isNexus.set(true);
                System.out.println("Nexuse asukoht valitud.");
                animate.start();
            } else {
                int i = convertPixelToIndex((e.getX()));
                int j = convertPixelToIndex(e.getY());
                Block eventBlock = map.getMap_matrix()[i][j];
                for (Block tower : map.getTowers()) {
                    tower.setActive(false);
                }
                if (eventBlock.getId() < 1){
                    eventBlock.makeTower(10,i*map.getSize()+map.getSize()/2,j*map.getSize()+map.getSize()/2);
                    //map.drawBlock(convertPixelToIndex((e.getX())),convertPixelToIndex(e.getY()));
                    map.getTowers().add(eventBlock);
                    eventBlock.setActive(true);
                }
                else if (eventBlock.getId()>=10){
                    eventBlock.setActive(true);
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

