import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class Game extends Canvas implements Runnable {
    private static int raha = 500;
    private Thread towerDefense;
    private boolean running;
    private int TowerToMakeId = 10;
    private Map map;
    private CanvasWindow cWindow;
    private static int health = 100;
    private int blockSize;

    public Game(int pixelWidth, int pixelHeight, int blockSize) {
        this.blockSize = blockSize;
        this.setWidth(pixelWidth);
        this.setHeight(pixelHeight);
        //Mapi suurused on blokkidena väljendatud!
        int mapWidth = (int) Math.ceil((double) pixelWidth / blockSize);
        int mapHeight = (int) Math.ceil((double) pixelHeight / blockSize);
        this.map = new Map(mapWidth, mapHeight, this.getGraphicsContext2D(), blockSize);
        cWindow = new CanvasWindow(this, blockSize);
    }

    public static int pixelToIndex(int pixel, int blockSize) {
        return (int) Math.floor((double) pixel / blockSize);
    }

    //Annab indexi keskkoha piksli.
    public static int indexToPixel(int index, int blockSize) {
        return index * blockSize + blockSize / 2;
    }

    public static void updateMoney(int money, Canvas c) {
        GraphicsContext g = c.getGraphicsContext2D();
        raha += money;
        String text = String.format("Raha: %s $", raha);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, (double) Main.getScreenH() / 20));
        g.setFill(Paint.valueOf("#2aa32e"));
        g.fillText(text, c.getWidth() - 300, 50);
    }

    public static int getRaha() {
        return raha;
    }

    public synchronized void start() {
        if (running) return;
        towerDefense = new Thread(this, "towerDefense");
        running = true;
        towerDefense.start();
    }

    public synchronized void stop() throws InterruptedException {
        if (!running) return;
        running = false;
        towerDefense.join();
    }

    public Map getMap() {
        return map;
    }

    public void generateMap() {
        map.initMap();
        map.genMap(2);
        map.genFlippedMap();
        map.drawMap();
    }

    public void chooseNexus(MouseEvent e) {
        int x = pixelToIndex((int) e.getX(), this.blockSize);
        int y = pixelToIndex((int) e.getY(), this.blockSize);
        Block eventBlock = map.getBlock(x, y);
        if (eventBlock.getId() != 0) {
            PopUp.createPopup("Valitud nexuse asukoht ei sobi! \nProovi uuesti!", true);
        } else {
            eventBlock.reconstruct(Blocks.NEXUS);
            map.setNexus(eventBlock);
            map.drawMap();
        }
    }

    public void runDijkstra() {
        map.runDijkstra();
    }

    public void clickDurigGame(MouseEvent e) {
        int clickx = (int) e.getX();
        int clicky = (int) e.getY();

        if (e.getButton() == MouseButton.SECONDARY && !cWindow.isClickOnWindow(clickx, clicky)) {
            cWindow.setActive(true);
            cWindow.setShow_tower(false);
            cWindow.setH(cWindow.getText_size() * 2);
            cWindow.setW((int) (this.getWidth() / 12));
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
            int x = pixelToIndex(clickx, this.blockSize);
            int y = pixelToIndex(clicky, this.blockSize);
            //Blokk, millel klikkati.
            Block eventBlock = map.getBlock(x, y);
            if (eventBlock.getId() == 0 || eventBlock.getId() == 9) {
                if (this.raha >= Towers.values()[TowerToMakeId].getHind()) {
                    this.raha -= Towers.values()[TowerToMakeId].getHind();
                    List<Spawnpoint> updatableSpawns = map.pathsContain(new int[]{x, y});
                    eventBlock.reconstruct(1, 0, Color.BLACK, 0);
                    //map.editMap_matrix(x, y, new Block(1, 0, Color.BLACK, 0));

                    if (/*eiTakistaTeed(updatableSpawns)*/ true) {
                        //genNewPaths(updatableSpawns);
                        map.noTowerRanges();
                        //Uue toweri genereerimine.
                        int towerX = x * map.getSize() + map.getSize() / 2;
                        int towerY = y * map.getSize() + map.getSize() / 2;
                        Tower newTower = new Tower(Towers.values()[TowerToMakeId], towerX, towerY, blockSize);
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

    public static void updateHealth(int hp, Canvas c) {
        GraphicsContext g = c.getGraphicsContext2D();
        health += hp;
        String text = String.format("Tervis: %s", health);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, c.getHeight() / 20));
        g.setFill(Paint.valueOf("#db1818"));
        g.fillText(text, c.getWidth() - 300, 100);
    }

    public void setTowerToMakeId(int towerToMakeId) {
        TowerToMakeId = towerToMakeId;
    }

    //Kõik siin sees hakkab pihta kui mäng alustatakse.
    @Override
    public void run() {
        while (running) {
            try {
                System.out.println(Thread.currentThread());
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ouch i am ded");
    }

}
