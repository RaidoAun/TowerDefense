import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

import java.util.List;
import java.util.Random;
import java.util.function.IntConsumer;

public class Game {
    private static int TowerToMakeId;
    private static Map map = Main.getMap();
    private static Canvas canvas = Main.getCanvas();
    private static GraphicsContext g = canvas.getGraphicsContext2D();
    private static int raha = 0;
    private static int spawnspeed = 1; //Mitme sekundi tagant spawnib üks monster!!
    private static CanvasWindow cWindow;
    private static int health;

    public static Scene getGameScene() {
        GridPane game_layout = new GridPane();
        game_layout.getChildren().add(canvas);
        return new Scene(game_layout);
    }

    public static void generateGame() {

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
        updateMoney(500);
        updateHealth(100);
    }

    public static boolean chooseNexus(int xPixel, int yPixel) {
        int x = convertPixelToIndex(xPixel);
        int y = convertPixelToIndex(yPixel);
        Block eventBlock = map.getMap_matrix()[x][y];
        map.setNexusxy(new int[]{x, y});
        if (eventBlock.getId() != 0) {
            PopUp.createPopup("Valitud nexuse asukoht ei sobi! (sein)\nProovi uuesti!", true);
        } else if (map.getSpawnpoints().get(0).genPathReturn(0).length == 0) {
            PopUp.createPopup("Valitud nexuse asukoht ei sobi! (no path)\nProovi uuesti!", true);
        } else {
            map.genNexus();
            map.genPathstoNexus(500);
            for (Spawnpoint spawn : map.getSpawnpoints()) {
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
                updateHealth(0);

                for (Spawnpoint spawn : map.getSpawnpoints()) {
                    if (generateMonster) {
                        Monsters[] monsters = Monsters.values();
                        spawn.genMonster(monsters[r.nextInt(monsters.length)]);
                    }
                    spawn.moveMonsters();
                    spawn.drawMonsters();
                    spawn.monstersTakeDamage();
                }
                cWindow.draw();
                if (generateMonster) generateMonster = false;
                if (health <= 0) {
                    this.stop();
                    PopUp.createPopup("YOU SUCK! GAME OVER!", false);
                }
            }
        };
        animate.start();


        canvas.setOnMouseClicked(e -> {
            int clickx = (int) e.getX();
            int clicky = (int) e.getY();

            if (!cWindow.isClickOnWindow(clickx, clicky)) {
                if (e.getButton() == MouseButton.SECONDARY){
                    cWindow.setActive(true);
                    cWindow.setShow_tower(false);
                    cWindow.setCoords(clickx-cWindow.getW()/2,clicky-cWindow.getH());
                    cWindow.setH(cWindow.getText_size()*2);
                    CanvasButton[] buttons_temp = new CanvasButton[Towers.values().length];
                    for (int i = 0; i < Towers.values().length; i++) {
                        int index = i;
                        CanvasButton button_temp = new CanvasButton(()->setTowerToMakeId(index));
                        button_temp.setColor(Towers.values()[index].getColor());
                        int button_padding = cWindow.getW()/Towers.values().length;
                        button_temp.setCoords(cWindow.getX()+button_padding*index,cWindow.getY()+cWindow.getH()-cWindow.getText_size(),cWindow.getText_size(),cWindow.getText_size());
                        buttons_temp[i] = button_temp;
                    }
                    cWindow.setButtons(buttons_temp);
                }
                if (e.getButton() == MouseButton.PRIMARY){
                    cWindow.setActive(false);

                    //Pikslite muutmine mapi maatriksi indexiteks.
                    int x = convertPixelToIndex(clickx);
                    int y = convertPixelToIndex(clicky);

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

                        //Blokk, millel klikkati.
                        Block eventBlock = map.getMap_matrix()[x][y];
                        for (Tower tower : map.getTowers()) {
                            tower.setActive(false);

                        }
                        if (eventBlock.getId() == 0 || eventBlock.getId() == 9) {
                            double towerX = x * map.getSize() + (double) map.getSize() / 2;
                            double towerY = y * map.getSize() + (double) map.getSize() / 2;
                            Tower newTower = new Tower(Towers.values()[TowerToMakeId], towerX, towerY);
                            map.getTowers().add(newTower);
                            map.editMap_matrix(x, y, newTower.getBlock());
                            newTower.setActive(true);
                        } else if (eventBlock.getId() >= 10) {
                            Tower currentTower = map.getTowerWithXY(x, y);
                            currentTower.setActive(true);
                            cWindow.setTower(currentTower);
                            cWindow.setActive(true);
                            cWindow.setShow_tower(true);
                        }
                    } else {
                        PopUp.createPopup("Towerit pole võimalik maha panna.\nProovi uuesti.", true);
                    }
                }
            }else{
                cWindow.checkButtons(clickx,clicky);
            }
        });
    }

    private static int getBlockSize() {
        Rectangle2D screenSizes = Screen.getPrimary().getBounds();
        return (int) Math.min(screenSizes.getWidth() / Main.getMap().getX(), screenSizes.getHeight() / Main.getMap().getY());
    }

    private static int convertPixelToIndex(int pixel_coords) {
        return pixel_coords / map.getSize();
    }

    private static void drawTowerRanges() {
        for (Tower tower : map.getTowers()) {
            if (tower.isActive()) {
                tower.drawRange();
            }
        }
    }

    public static void updateMoney(int money) {
        raha += money;
        String text = String.format("Raha: %s $", raha);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, canvas.getWidth() / 40));
        g.setFill(Paint.valueOf("#2aa32e"));
        g.fillText(text, canvas.getWidth() * 0.85, canvas.getHeight() * 0.05);
    }

    public static void updateHealth(int hp) {
        health += hp;
        String text = String.format("Tervis: %s", health);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, canvas.getWidth() / 40));
        g.setFill(Paint.valueOf("#db1818"));
        g.fillText(text, canvas.getWidth() * 0.85, canvas.getHeight() * 0.10);
    }

    public static void setTowerToMakeId(int towerToMakeId) {
        TowerToMakeId = towerToMakeId;
    }
}
