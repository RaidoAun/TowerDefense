package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import java.util.List;

public class Game {

    private static Map map = Main.getMap();
    private static Canvas canvas = Main.getCanvas();
    private static GraphicsContext g = canvas.getGraphicsContext2D();
    private static int raha = 0;
    private static int spawnspeed = 1; //Mitme sekundi tagant spawnib üks monster!!

    public static Scene getGameScene() {
        GridPane game_layout = new GridPane();
        game_layout.getChildren().add(canvas);
        return new Scene(game_layout);
    }

    public static void generateGame(){

        double blocksize = getBlockSize();
        map.initMap();
        map.genMap(2);
        map.genFlippedMap();
        //Genereerib nii palju spawnpointe, kui võimalik on.
        map.genOpenBlocks();
        map.generateSpawnpoints();
        map.spawnSpawnpoints();
        map.drawMap(blocksize);
    }

    public static boolean chooseNexus(double xPixel, double yPixel) {
        int x = convertPixelToIndex(xPixel);
        int y = convertPixelToIndex(yPixel);
        Block eventBlock = map.getMap_matrix()[x][y];
        map.setNexusxy(new int[]{x, y});
        if (eventBlock.getId() != 0) {
            PopUp.createPopup("Valitud nexuse asukoht ei sobi! (sein)\nProovi uuesti!");
        } else if (map.getSpawnpoints().get(0).genPathReturn(0).length == 0) {
            PopUp.createPopup("Valitud nexuse asukoht ei sobi! (no path)\nProovi uuesti!");
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

        long startNanoTime = System.nanoTime();

        AnimationTimer animate = new AnimationTimer() {
            double blocksize = getBlockSize();
            boolean genereateMonster = false;
            long timeStamp = spawnspeed;
            public void handle(long currentNanoTime) {
                long t = (currentNanoTime - startNanoTime) / 1000000000;
                if (t == timeStamp) {
                    timeStamp += spawnspeed;
                    genereateMonster = true;
                }
                map.drawMap(blocksize);
                drawTowerRanges();
                updateMoney(4000);
                for (Spawnpoint spawn : map.getSpawnpoints()) {
                    if (genereateMonster) {
                        spawn.genMonster();
                    }
                    spawn.moveMonsters();
                    spawn.drawMonsters();
                    spawn.shootTowers();
                }
                if (genereateMonster) genereateMonster = false;
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
        return Math.min(screenSizes.getWidth() / Main.getMap().getX(), screenSizes.getHeight() / Main.getMap().getY());
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

    public static void updateMoney(int money) {
        String text = String.format("Raha: %s $", money);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, 50));
        g.setFill(Paint.valueOf("#2aa32e"));
        g.fillText(text, 1600, 75);
    }

    public static int getRaha() {
        return raha;
    }

}
