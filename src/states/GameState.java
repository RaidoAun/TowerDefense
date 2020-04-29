package states;

import blocks.Block;
import blocks.Spawnpoint;
import blocks.towers.*;
import entities.Entity;
import entities.Monster;
import gui.CanvasButton;
import gui.CanvasWindow;
import gui.PopUp;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import map.Map;
import tools.Click;
import towerdefense.Main;

import java.util.ArrayList;
import java.util.List;

public class GameState implements State {

    public static int raha = 1000;
    public static int health = 100;
    private StateManager sm;
    private Scene gameScene;
    private States game;
    private Map map;
    private CanvasWindow cWindow;
    private int towerToMakeId;
    private Click click;

    public GameState(StateManager sm, Scene gameScene, Map map) {

        this.sm = sm;
        this.game = States.GAME;
        this.map = map;
        this.gameScene = gameScene;
        this.cWindow = new CanvasWindow(sm.getCanvas(), map);
        this.towerToMakeId = 0;

        sm.getCanvas().setOnMouseClicked(e -> click = new Click(e, map));

    }

    public static void updateMoney(int money) {
        raha += money;
    }

    public static void updateHealth(int hp) {
        health += hp;
    }

    private void drawHealth(GraphicsContext g) {
        String text = String.format("Tervis: %s", health);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, (double) Main.screenH / 20));
        g.setFill(Paint.valueOf("#db1818"));
        g.fillText(text, Main.screenW - 300, 100);
    }

    private void drawMoney(GraphicsContext g) {
        String text = String.format("Raha: %s $", raha);
        g.setFont(Font.font("Calibri", FontWeight.BOLD, (double) Main.screenH / 20));
        g.setFill(Paint.valueOf("#2aa32e"));
        g.fillText(text, Main.screenW - 300, 50);
    }

    @Override
    public void tick() {
        //Kontrollib, kas mäng on läbi.
        if (health <= 0) {
            Platform.runLater(() -> PopUp.createPopup("YOU SUCK! GAME OVER!"));
            sm.resetStates(States.GAME, States.NEXUS);
            sm.setState(States.MENU);
            return;
        }
        //Vaatab, kas mängija on ekreaanil klikanud, ja kui on, siis uuendab väärtusi.
        if (click != null) {
            towerClick();
        }
        //Uute monsterite spawnimine.
        for (Spawnpoint spawn : map.getSpawnpoints()) {
            spawn.tick();
        }
        //Koletiste liigutamine.
        List<Monster> monsters = new ArrayList<>(map.getAllMonsters());
        for (Monster monster : monsters) {
            monster.tick(map);
        }
        //Towerite tulistamine (ei ole graafiline).
        for (Tower tower : map.getTowers()) {
            tower.tick(map);
        }
    }

    @Override
    public void render() {
        GraphicsContext g = sm.getCanvas().getGraphicsContext2D();
        //Vajaduse scene vahetamine.
        if (sm.getWindow().getScene() != gameScene) {
            sm.changeScene(gameScene);
            sm.toggleFullscreen();
        }
        //Mapi joonistamine.
        map.drawMap(Main.blockSize);
        map.drawGrid(0.1, Color.BLACK);
        //Tower rangede joonistamine.
        renderTowerRanges();
        //Raha ja elude uuendamine graafiliselt.
        drawMoney(g);
        drawHealth(g);

        for (Monster monster : map.getAllMonsters()) {
            monster.render(g);
        }
        //Towerite graafika joonistamine.
        for (Tower tower : map.getTowers()) {
            tower.render(g);
        }
        //CanvasWindow joonistamine.
        cWindow.draw();
    }

    @Override
    public States getState() {
        return game;
    }

    @Override
    public void reset() {
        map = null;
        click = null;
        raha = 1000;
        health = 100;
    }

    private void towerClick() {
        //Pikslid!!!
        int clickx = click.getPixelX();
        int clicky = click.getPixelY();
        //If spagett, mis vaatab, kas klikati CanvasWindow peale.
        if (click.secondary && !cWindow.isClickOnWindow(clickx, clicky)) {
            openTowerMenu();
        } else if (click.primary && !cWindow.isClickOnWindow(clickx, clicky)) {
            Block eventBlock = click.eventblock;
            cWindow.setActive(false);
            //Indexid!!!
            int x = click.indexX;
            int y = click.indexY;
            //If spagett, mis vaatab kas towerit saab ehitada, upgradeda.
            if (eventBlock.getId() != 0 && eventBlock.getId() != 9 && eventBlock.getId() < 10) {
                map.noTowerRanges();
                Platform.runLater(() -> PopUp.createPopup("Sellele ruudule ei saa ehitada!\n Proovi uuesti!"));
            } else if (raha < Towers.values()[towerToMakeId].getHind()) {
                Platform.runLater(() -> PopUp.createPopup("Pole piisavalt raha, et ehitada!"));
            } else if (eventBlock.getId() >= 10) {
                upgradeTower(x, y);
            } else {
                placeTower(x, y);
            }
        } else {
            cWindow.checkButtons(clickx, clicky);
        }
        click = null;
    }

    private void openTowerMenu() {
        cWindow.setActive(true);
        cWindow.setShow_tower(false);
        cWindow.setH(cWindow.getText_size() * 2);
        cWindow.setW(Main.screenW / 12);
        cWindow.setCoords(click.getPixelX() - cWindow.getW() / 2, click.getPixelY() - cWindow.getH());
        CanvasButton[] buttons_temp = new CanvasButton[Towers.values().length];
        for (int i = 0; i < Towers.values().length; i++) {
            int index = i;
            CanvasButton button_temp = new CanvasButton(() -> towerToMakeId = index);
            button_temp.setColor(Towers.values()[index].getColor());
            int button_padding = cWindow.getW() / Towers.values().length;
            button_temp.setCoords((int) (cWindow.getX() + button_padding * (index + 0.5)), cWindow.getY() + cWindow.getH() / 2 - cWindow.getText_size() / 2, cWindow.getText_size(), cWindow.getText_size());
            buttons_temp[i] = button_temp;
        }
        cWindow.setButtons(buttons_temp);
    }

    private void placeTower(int x, int y) {

        List<Spawnpoint> updatableSpawns = map.pathsContain(new int[]{x, y});
        map.editMap_matrix(x, y, new Block(x, y, 1, 0, Color.BLACK, 0));
        if (eiTakistaTeed(updatableSpawns)) {
            Towers towerToMake = Towers.values()[towerToMakeId];
            raha -= towerToMake.getHind();
            genNewPaths(updatableSpawns);
            map.noTowerRanges();
            //Uue toweri genereerimine.
            Tower newTower = TowerChooser.getTower(towerToMake, x, y);
            //Error saab ainult siis tulla, kui enum väärus tower ei eksisteeri TowerChooseris.
            if (newTower != null) {
                map.getTowers().add(newTower);
                map.editMap_matrix(x, y, newTower.getBlock());
                newTower.setActive(true);
            } else {
                System.err.println(towerToMake.getNimi() + " ei ole olemas!");
            }
        } else {
            map.editMap_matrix(x, y, click.eventblock);
            Platform.runLater(() -> PopUp.createPopup("Tower takistaks spawnpointi teed nexuseni!\nProovi uuesti!"));
            map.noTowerRanges();
        }
    }

    private void upgradeTower(int x, int y) {
        map.noTowerRanges();
        Tower currentTower = map.getTowerWithXY(x, y);
        currentTower.setActive(true);
        cWindow.setTower(currentTower);
        cWindow.setActive(true);
        cWindow.setShow_tower(true);
    }

    private void renderTowerRanges() {
        for (Tower tower : map.getTowers()) {
            if (tower.isActive()) {
                tower.drawRange(sm.getCanvas().getGraphicsContext2D());
            }
        }
    }

    public void genNewPaths(List<Spawnpoint> spawns) {
        for (Spawnpoint s : spawns) {
            int[][] oldPath = s.getPath();
            s.genPath(500);
            map.deletePath(oldPath);
            map.drawPath(s.getPath());
        }
    }

    private boolean eiTakistaTeed(List<Spawnpoint> spawns) {
        for (Spawnpoint s : spawns) {
            if (s.genPathReturn(1).length == 0) {
                return false;
            }
        }
        return true;
    }

}
