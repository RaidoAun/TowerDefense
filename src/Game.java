import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Random;

public class Game {
    private static int TowerToMakeId;
    private static Map map;
    private static Canvas canvas;
    private static GraphicsContext g;
    private static int raha;
    private static int spawnspeed; //Mitme sekundi tagant spawnib üks monster!!
    private static CanvasWindow cWindow;
    private static int health;
    private static double lastFrameTime;
    private static AnimationTimer animate;
    private static int blockSize;

    public static void setValues() {

        blockSize = Main.getBlockSize();
        int x = (int) Math.ceil((double) Main.getScreenW() / blockSize);
        int y = (int) Math.ceil((double) Main.getScreenH() / blockSize);
        canvas = new Canvas();
        map = new Map(x, y, canvas, blockSize);

        TowerToMakeId = 0;
        g = canvas.getGraphicsContext2D();
        raha = 500;
        spawnspeed = 1;
        cWindow = new CanvasWindow(canvas);
        health = 100;
        lastFrameTime = 0;

        animate = new AnimationTimer() {

            Random r = new Random();
            long startTime;
            long endTime;
            boolean generateMonster = false;
            boolean shoot = false;
            long lastSpawnTime = 0;
            long lastShootTime = 0;
            long startNanoTime = System.nanoTime();

            @Override
            public void handle(long now) {
                startTime = System.nanoTime();
                long t = (now - startNanoTime) / 1000000000;
                if (t % spawnspeed == 0 && t != lastSpawnTime) {
                    generateMonster = true;
                    lastSpawnTime = t;
                }
                if (t != lastShootTime) {
                    shoot = true;
                    lastShootTime = t;
                }
                map.drawMap();
                tickTowers();

                updateMoney(0);
                updateHealth(0);

                for (Spawnpoint spawn : map.getSpawnpoints()) {
                    if (generateMonster) {
                        Monsters[] monsters = Monsters.values();
                        spawn.genMonster(monsters[r.nextInt(monsters.length)]);
                    }
                }

                map.moveMonsters();
                map.drawMonsters();
                map.monstersTakeDamage(!shoot);
                cWindow.draw();
                if (generateMonster) generateMonster = false;
                if (shoot) shoot = false;
                if (health <= 0) {
                    this.stop();
                    PopUp.createPopup("YOU SUCK! GAME OVER!", false);
                }
                endTime = System.nanoTime();
                lastFrameTime = (double) (endTime - startTime) / 1000000000;
            }
        };

    }

    public static GraphicsContext getG() {
        return g;
    }

    public static void resumeAnimation() {
        animate.start();
    }

    public static Scene getGameScene() {
        GridPane game_layout = new GridPane();
        game_layout.getChildren().add(canvas);
        Scene scene = new Scene(game_layout);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                Main.toggleFullscreen();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                animate.stop();
                Main.getWindow().setScene(Pause.getPauseScreen());
                Main.getWindow().setFullScreen(true);
            }
        });
        return scene;
    }

    public static Canvas getCanvas() {
        return canvas;
    }

    public static int getBlockSize() {
        return blockSize;
    }

    public static Map getMap() {
        return map;
    }

    public static void generateMap() {
        map.initMap();
        map.genMap(2);
        map.genFlippedMap();
        map.drawMap();
    }

    public static void chooseNexus(MouseEvent e) {
        int x = pixelToIndex((int) e.getX());
        int y = pixelToIndex((int) e.getY());
        Block eventBlock = map.getBlock(x, y);
        if (eventBlock.getId() != 0) {
            PopUp.createPopup("Valitud nexuse asukoht ei sobi! \nProovi uuesti!", true);
        } else {
            eventBlock.reconstruct(Blocks.NEXUS);
            map.setNexus(eventBlock);
            map.drawMap();
        }
    }

    public static void runDijkstra() {
        map.runDijkstra();
    }

    public static void clickDurigGame(MouseEvent e) {
        int clickx = (int) e.getX();
        int clicky = (int) e.getY();

        if (e.getButton() == MouseButton.SECONDARY && !cWindow.isClickOnWindow(clickx, clicky)) {
            cWindow.setActive(true);
            cWindow.setShow_tower(false);
            cWindow.setH(cWindow.getText_size() * 2);
            cWindow.setW((int) (canvas.getWidth() / 12));
            cWindow.setCoords(clickx - (double) cWindow.getW() / 2, clicky - cWindow.getH());
            CanvasButton[] buttons_temp = new CanvasButton[Towers.values().length];
            for (int i = 0; i < Towers.values().length; i++) {
                int index = i;
                CanvasButton button_temp = new CanvasButton(() -> setTowerToMakeId(index));
                button_temp.setColor(Towers.values()[index].getColor());
                int button_padding = cWindow.getW() / Towers.values().length;
                button_temp.setCoords((int) (cWindow.getX() + button_padding * (index + 0.5)), cWindow.getY() + cWindow.getH() / 2 - cWindow.getText_size() / 2, cWindow.getText_size(), cWindow.getText_size());
                buttons_temp[i] = button_temp;
            }
            cWindow.setButtons(buttons_temp);
        } else if (e.getButton() == MouseButton.PRIMARY && !cWindow.isClickOnWindow(clickx, clicky)) {
            cWindow.setActive(false);

            //Pikslite muutmine mapi maatriksi indexiteks.
            int x = pixelToIndex(clickx);
            int y = pixelToIndex(clicky);
            //Blokk, millel klikkati.
            Block eventBlock = map.getBlock(x, y);
            if (eventBlock.getId() == 0 || eventBlock.getId() == 9) {
                if (Game.raha >= Towers.values()[TowerToMakeId].getHind()) {
                    Game.raha -= Towers.values()[TowerToMakeId].getHind();
                    List<Spawnpoint> updatableSpawns = map.pathsContain(new int[]{x, y});
                    eventBlock.reconstruct(1, 0, Color.BLACK, 0);
                    //map.editMap_matrix(x, y, new Block(1, 0, Color.BLACK, 0));

                    if (/*eiTakistaTeed(updatableSpawns)*/ true) {
                        //genNewPaths(updatableSpawns);
                        map.noTowerRanges();
                        //Uue toweri genereerimine.
                        int towerX = x * map.getSize() + map.getSize() / 2;
                        int towerY = y * map.getSize() + map.getSize() / 2;
                        Tower newTower = new Tower(Towers.values()[TowerToMakeId], towerX, towerY);
                        map.getTowers().add(newTower);
                        map.editMap_matrix(x, y, newTower);
                        newTower.setActive(true);

                    } else {
                        map.editMap_matrix(x, y, eventBlock);
                        PopUp.createPopup("Tower takistaks spawnpointi teed nexuseni!\nProovi uuesti!", true);
                        map.noTowerRanges();
                    }

                }
            } else if (eventBlock.getId() >= 10) {
                map.noTowerRanges();
                Tower currentTower = map.getTowerWithXY(x, y);
                currentTower.setActive(true);
                cWindow.setTower(currentTower);
                cWindow.setActive(true);
                cWindow.setShow_tower(true);
            } else {
                map.noTowerRanges();
                PopUp.createPopup("Sellele ruudule ei saa ehitada!\n Proovi uuesti!", true);
            }
        } else {
            cWindow.checkButtons(clickx, clicky);
        }
    }

    public static int pixelToIndex(int pixel) {
        return (int) Math.floor((double) pixel / blockSize);
    }

    //Annab indexi keskkoha piksli.
    public static int indexToPixel(int index) {
        return index * blockSize + blockSize / 2;
    }

    private static void tickTowers() {
        for (Tower tower : map.getTowers()) {
            if (tower.isActive()) {
                tower.drawRange();
            }
        }
    }

    public static void updateMoney(int money) {
        raha += money;
        String text = String.format("Raha: %s $", raha);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, (double) Main.getScreenH() / 20));
        g.setFill(Paint.valueOf("#2aa32e"));
        g.fillText(text, canvas.getWidth() - 300, 50);
    }

    public static void updateHealth(int hp) {
        health += hp;
        String text = String.format("Tervis: %s", health);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, (double) Main.getScreenH() / 20));
        g.setFill(Paint.valueOf("#db1818"));
        g.fillText(text, canvas.getWidth() - 300, 100);
    }

    public static void setTowerToMakeId(int towerToMakeId) {
        TowerToMakeId = towerToMakeId;
    }

    public static double getLastFrameTime() {
        return lastFrameTime;
    }

    public static int getRaha() {
        return raha;
    }
}
