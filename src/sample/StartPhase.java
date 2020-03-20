package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//Mõte oleks, et see klass töötaks start nupu klikimisest kuni esimese roundi alguseni.
// Ükejäänud funktsioonid asuksid mujal klassis

public class StartPhase {

    private static Map map = Main.getMap();
    private static Canvas canvas = Main.getCanvas();

    public static Scene getGameScene() {
        GridPane game_layout = new GridPane();
        game_layout.getChildren().add(Main.getCanvas());
        return new Scene(game_layout);
    }

    private static double getBlockSize() {
        Rectangle2D screenSizes = Screen.getPrimary().getBounds();
        return Math.min(screenSizes.getWidth() / Main.map.getX(), screenSizes.getHeight() / Main.map.getY());
    }

    public static void startGame(){
        double blocksize = getBlockSize();
        AnimationTimer animate = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                long startNanoTime = System.nanoTime();
                map.drawMap(blocksize);
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
        map.genMap(2);
        map.genFlippedMap();
        //Genereerib nii palju spawnpointe, kui võimalik on.
        map.genOpenBlocks();
        map.generateSpawnpoints(5, 40);
        map.spawnSpawnpoints();
        map.drawMap(blocksize);


        System.out.println("Vali nexuse asukoht kaardil!");
        AtomicBoolean isNexus = new AtomicBoolean(false);
        map.drawMap(blocksize);
        canvas.setOnMouseClicked(e -> {

            int x = convertPixelToIndex(e.getX());
            int y = convertPixelToIndex(e.getY());
            Block eventBlock = map.getMap_matrix()[x][y];

            if (!isNexus.get()) {
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
                System.out.println(eventBlock.getId());

                //Spawnpoindid, mille teele jääb tower ette.
                List<Spawnpoint> updatableSpawns = map.pathsContain(new int[]{x, y});
                //System.out.println(updatableSpawns.size());
                boolean towerPossible = true;
                //Ajutise seina loomine.
                Block oldBlock = map.getBlock(x, y);
                map.editMap_matrix(x, y, new Block(1, 0, new Color(0, 0, 0, 1)));
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
                        map.deletePath(oldPath);
                        map.drawPath(spawn.getPath());
                    }
                }

                map.editMap_matrix(x, y, oldBlock);

                if (towerPossible) {
                    for (Block tower : map.getTowers()) {
                        tower.setActive(false);
                    }
                    if (eventBlock.getId() == 0 || eventBlock.getId() == 9){
                        //map.editMap_matrix(i, j, new Block(10, 0, new Color(0, 0, 0, 1)));
                        eventBlock.makeTower(10,x*map.getSize()+map.getSize()/2,y*map.getSize()+map.getSize()/2);
                        map.editMap_matrix(x, y, eventBlock);
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
    }

    private static int convertPixelToIndex(double pixel_coords){
        return (int) (pixel_coords/map.getSize());
    }

    private static void drawTowerRanges(){
        for (Block tower: map.getTowers()) {
            if (tower.getActive()){
                tower.drawRange();
            }
        }
    }

}
