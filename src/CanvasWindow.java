import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CanvasWindow {

    private int x;
    private int y;
    private int w;
    private int h;
    private Canvas c;
    private boolean active;
    private Color color = new Color(1, 1, 1, 0.95);
    private GraphicsContext gc;
    private int block_size;
    private int text_size;
    private Tower tower;
    private CanvasButton[] buttons;
    private Boolean show_tower;

    CanvasWindow(Canvas c) {
        this.c = c;
        this.gc = c.getGraphicsContext2D();
        this.block_size = Main.getMap().getSize();
        this.text_size = (int) (c.getWidth() / 100);
    }

    public void draw() {
        if (this.active) {
            gc.setFill(color);
            gc.fillRect(x, y, w, h);
            if (show_tower){
                drawTowerInfo();
            }
            drawButtons();
        }
    }

    public void drawTowerInfo() {
        String[] info = new String[]{"Id:", "Dmg:", "Range:", "Level:"};
        String[] value = new String[]{Integer.toString(tower.getId()), Integer.toString(this.tower.getDamage()), Double.toString(this.tower.getRange()), Integer.toString(this.tower.getLevel())};
        gc.setFont(Font.font("Calibri", FontWeight.BOLD, this.text_size));
        gc.setFill(Paint.valueOf("#2aa32e"));
        for (int i = 0; i < info.length; i++) {
            gc.fillText(info[i], this.x + (double) this.text_size / 2, this.y + this.text_size * (i + 1));
            gc.fillText(value[i], this.x + this.text_size * 4, this.y + this.text_size * (i + 1));
        }
    }
    public void drawButtons(){

        for (int i = 0; i < buttons.length; i++) {
            gc.setFill(this.buttons[i].getColor());
            gc.fillRect(this.buttons[i].getX(), this.buttons[i].getY(), this.buttons[i].getW(), this.buttons[i].getH());
        }

    }

    public boolean isClickOnWindow(int x, int y) {
        if (x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.h) {
            return true;
        } else {
            return false;
        }
    }
    public void checkButtons(int clickx, int clicky){
        for (int i = 0; i < this.buttons.length; i++) {
            if (clickx > this.buttons[i].getX() && clickx < this.buttons[i].getX() + this.buttons[i].getW() && clicky > this.buttons[i].getY() && clicky < this.buttons[i].getY()+this.buttons[i].getH()) {
                this.buttons[i].onPressed();
            }
        }
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
        this.w = (int) (c.getWidth() / 12);
        this.h = (int) (this.w * 0.8);
        int tempx = (int) (tower.getPixelX() - this.w / 2);
        int tempy = (int) (this.tower.getPixelY() - this.block_size * 2 - this.h);
        this.x = Math.max(tempx, 0);
        if (tempy < 0) {
            this.y = (int) (this.tower.getPixelY() + 3 * this.block_size);
        } else {
            this.y = tempy;
        }
        CanvasButton temp = new CanvasButton(tower::lvlUp);
        temp.setCoords(this.x + this.w / 2 - this.text_size / 2, this.y + this.h - this.text_size, this.text_size, this.text_size);
        this.buttons = new CanvasButton[]{temp};
    }

    public void setShow_tower(Boolean show_tower) {
        this.show_tower = show_tower;
    }
    public void setCoords(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
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

    public void setH(int h) {
        this.h = h;
    }

    public void setW(int w) {
        this.w = w;
    }
}
