package gui;

import blocks.towers.Tower;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import states.GameState;
import towerdefense.Main;

public class CanvasWindow {

    private int x;
    private int y;
    private int w;
    private int h;
    private boolean active;
    private Color color = new Color(1, 1, 1, 0.95);
    private GraphicsContext gc;
    private int block_size;
    private int text_size;
    private Tower tower;
    private CanvasButton[] buttons;
    private Boolean show_tower;

    public CanvasWindow(GraphicsContext g) {
        this.gc = g;
        this.block_size = Main.blockSize;
        this.text_size = Main.screenH / 60;
    }

    public void draw() {
        if (this.active) {
            gc.setFill(color);
            gc.fillRect(x, y, w, h);
            if (show_tower) {
                drawTowerInfo(this.tower);
            }
            drawButtons();
        }
    }

    public void drawTowerInfo(Tower tower) {
        String[] info = new String[]{tower.getName(), "Dmg:", "Range:", "Level:"};
        String[] value = new String[]{"", Integer.toString(tower.getDamage()), Double.toString(tower.getRange()/Main.blockSize), Integer.toString(tower.getLevel())};
        gc.setFont(Font.font("Calibri", FontWeight.BOLD, this.text_size));
        gc.setFill(tower.getColor());
        for (int i = 0; i < info.length; i++) {
            gc.fillText(info[i], this.x + (double) this.text_size / 2, this.y + this.text_size * (i + 1));
            gc.fillText(value[i], this.x + this.text_size * 4, this.y + this.text_size * (i + 1));
        }
    }
    public void drawTowerInfoALL(Tower tower) {
        String[] info = new String[]{tower.getName(),"Hind", "Dmg:", "Range:", "Level:",""};
        String[] value = new String[]{"", Integer.toString(tower.getHind()), Integer.toString(tower.getDamage()), Double.toString(tower.getRange()/Main.blockSize), Integer.toString(tower.getLevel()), "Upgrade"};
        gc.setFont(Font.font("Calibri", FontWeight.BOLD, this.text_size));
        gc.setFill(tower.getColor());
        for (int i = 0; i < info.length; i++) {
            gc.fillText(info[i], this.x + (double) this.text_size / 2, this.y + this.text_size * (i + 1));
            gc.fillText(value[i], this.x + this.text_size * 4, this.y + this.text_size * (i + 1));
        }
    }

    public void drawButtons() {
        for (CanvasButton button : buttons) {
            gc.setFill(button.getColor());
            gc.fillRect(button.getX(), button.getY(), button.getW(), button.getH());
        }

    }

    public boolean isClickOnWindow(int x, int y) {
        if (this.active){
            return x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.h;
        }
        else{
            return false;
        }
    }

    public void checkButtons(int clickx, int clicky) {
        for (CanvasButton button : this.buttons) {
            if (clickx > button.getX() && clickx < button.getX() + button.getW() && clicky > button.getY() && clicky < button.getY() + button.getH()) {
                button.onPressed();
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
        this.w = Main.screenH / 6;
        this.h = (int) (this.w * 0.6);
        int tempx = (int) tower.getPixelX() - this.w / 2;
        int tempy = (int) tower.getPixelY() - this.block_size * 2 - this.h;
        this.x = Math.max(tempx, 0);
        if (tempy < 0) {
            this.y = (int) tower.getPixelY() + 3 * this.block_size;
        } else {
            this.y = tempy;
        }
        CanvasButton temp = new CanvasButton(() -> {
            GameState.map.sellTower(this.tower);
            GameState.genNewPaths(GameState.map.getSpawnpoints());
            this.active = false;
        });
        CanvasButton temp2 = new CanvasButton(() -> {
            tower.lvlUp();
        });
        temp.setColor(Color.RED);
        temp.setCoords(this.x + this.w / 3 - this.text_size / 2, this.y + this.h - this.text_size, this.text_size, this.text_size);

        temp2.setColor(Color.GREEN);
        temp2.setCoords(this.x + 2 * (this.w / 3) - this.text_size / 2, this.y + this.h - this.text_size, this.text_size, this.text_size);
        this.buttons = new CanvasButton[]{temp, temp2};
    }

    public void setShow_tower(Boolean show_tower) {
        this.show_tower = show_tower;
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setButtons(CanvasButton[] buttons) {
        this.buttons = buttons;
    }

    public int getText_size() {
        return text_size;
    }

    public boolean isActive() {
        return active;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setText_size(int text_size) {
        this.text_size = text_size;
    }
}
