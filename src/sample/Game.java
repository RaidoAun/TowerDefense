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
import java.util.Random;

public class Game {

    private static Map map = Main.getMap();
    private static Canvas canvas = Main.getCanvas();
    private static GraphicsContext g = canvas.getGraphicsContext2D();
    private static int raha = 0;
    private static int spawnspeed = 1; //Mitme sekundi tagant spawnib üks monster!!
    private static CanvasWindow cWindow;

    public static Scene getGameScene() {
        GridPane game_layout = new GridPane();
        game_layout.getChildren().add(canvas);
        return new Scene(game_layout);
    }

    public static void generateGame(){

        int blocksize = getBlockSize();
        map.initMap();
        map.genMap(2);
        map.genFlippedMap();
        //Genereerib nii palju spawnpointe, kui võimalik on.
        map.genOpenBlocks();
        map.generateSpawnpoints();
        map.spawnSpawnpoints();
        map.drawMap(blocksize);
        cWindow = new CanvasWindow(canvas);
    }

    public static boolean chooseNexus(int xPixel, int yPixel) {
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
        Random r = new Random();

        AnimationTimer animate = new AnimationTimer() {
            int blocksize = getBlockSize();
            boolean generateMonster = false;
            long timeStamp = spawnspeed;
            public void handle(long currentNanoTime) {
                long t = (currentNanoTime - startNanoTime) / 1000000000;
                if (t == timeStamp) {
                    timeStamp += spawnspeed;
                    generateMonster = true;
                }
                map.drawMap(blocksize);
                drawTowerRanges();

                updateMoney(0);

                for (Spawnpoint spawn : map.getSpawnpoints()) {
                    if (generateMonster) {
                        Monsters[] monsters = Monsters.values();
                        spawn.genMonster(monsters[r.nextInt(monsters.length)]);
                    }
                    spawn.moveMonsters();
                    spawn.drawMonsters();
                    spawn.shootTowers();
                }
                cWindow.draw();
                if (generateMonster) generateMonster = false;
            }
        };
        animate.start();

        canvas.setOnMouseClicked(e -> {
            int clickx = (int) e.getX();
            int clicky = (int) e.getY();
            if (cWindow.isClickOnWindow(clickx,clicky)){

            }else{
                cWindow.setActive(false);
                int x = convertPixelToIndex(clickx);
                int y = convertPixelToIndex(clicky);
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
                    else if (eventBlock.getId()>=10){//UPGRADE
                        eventBlock.setActive(true);
                        cWindow.setTower(eventBlock);
                        cWindow.setActive(true);
                    }
                } else {
                    PopUp.createPopup("Towerit pole võimalik maha panna.\nProovi uuesti.");
                }
            }
        });

    }

    private static int getBlockSize() {
        Rectangle2D screenSizes = Screen.getPrimary().getBounds();
        return (int) Math.min(screenSizes.getWidth() / Main.getMap().getX(), screenSizes.getHeight() / Main.getMap().getY());
    }

    private static int convertPixelToIndex(int pixel_coords){
        return pixel_coords/map.getSize();
    }
    private static double convertIndexToPixel(int index){
        return index*map.getSize();
    }

    private static void drawTowerRanges(){
        for (Block tower: map.getTowers()) {
            if (tower.getActive()){
                tower.drawRange();
            }
        }
    }

    public static void updateMoney(int money) {
        raha += money;
        String text = String.format("Raha: %s $", raha);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, canvas.getWidth()/40));
        g.setFill(Paint.valueOf("#2aa32e"));
        g.fillText(text, canvas.getWidth()*0.85, canvas.getHeight()*0.05);
    }

    public static int getRaha() {
        return raha;
    }

}
