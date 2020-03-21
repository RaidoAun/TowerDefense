package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import java.util.List;

//Mõte oleks, et see klass töötaks start nupu klikimisest kuni esimese roundi alguseni.
// Ükejäänud funktsioonid asuksid mujal klassis

public class Game {

    private static Map map = Main.getMap();
    private static Canvas canvas = Main.getCanvas();

    public static Scene getGameScene() {
        GridPane game_layout = new GridPane();
        game_layout.getChildren().add(Main.getCanvas());
        return new Scene(game_layout);
    }

    public static void generateGame(){

        double blocksize = getBlockSize();
        map.initMap();
        map.genMap(2);
        map.genFlippedMap();
        //Genereerib nii palju spawnpointe, kui võimalik on.
        map.genOpenBlocks();
        map.generateSpawnpoints(5, 40);
        map.spawnSpawnpoints();
        map.drawMap(blocksize);
    }

    public static boolean chooseNexus(double xPixel, double yPixel) {
        int x = convertPixelToIndex(xPixel);
        int y = convertPixelToIndex(yPixel);
        Block eventBlock = map.getMap_matrix()[x][y];
        //Nexuse valimise klikk.
        map.setNexusxy(new int[]{x, y});
        if (eventBlock.isWall()) {
            PopUp.createPopup("Valitud nexuse asukoht ei sobi!\nProovi uuesti!");
            System.out.println("Sein!");
        } else if (map.getSpawnpoints().get(0).genPathReturn(0).length == 0) {
            PopUp.createPopup("Valitud nexuse asukoht ei sobi!\nProovi uuesti!");
        } else {
            map.genNexus();
            map.genPathstoNexus(500);
            for (Spawnpoint spawn: map.getSpawnpoints()) {
                map.drawPath(spawn.getPath());
            }
        }
        return map.getSpawnpoints().get(0).isNexusWithPath();
    }

    public static void startRounds() {

        AnimationTimer animate = new AnimationTimer() {
            double blocksize = getBlockSize();
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
        animate.start();

        canvas.setOnMouseClicked(e -> {
            int x = convertPixelToIndex(e.getX());
            int y = convertPixelToIndex(e.getY());
            Block eventBlock = map.getMap_matrix()[x][y];

            //Spawnpoindid, mille teele jääb tower ette.
            List<Spawnpoint> updatableSpawns = map.pathsContain(new int[]{x, y});
            boolean towerPossible = true;
            //Ajutise seina loomine.
            Block oldBlock = map.getBlock(x, y);
            map.editMap_matrix(x, y, new Block(1, 0, new Color(0, 0, 0, 1)));
            //Uuenda spawnpointide path.
            for (Spawnpoint spawn : updatableSpawns) {
                int[][] oldPath = spawn.getPath();
                spawn.genPath(500);
                if (spawn.getPath().length == 0) {
                    towerPossible = false;
                    spawn.setPath(oldPath);
                    break;
                } else {
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
            } else {
                PopUp.createPopup("Towerit pole võimalik maha panna.\nProovi uuesti.");
            }

        });

    }

    private static double getBlockSize() {
        Rectangle2D screenSizes = Screen.getPrimary().getBounds();
        return Math.min(screenSizes.getWidth() / Main.map.getX(), screenSizes.getHeight() / Main.map.getY());
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
