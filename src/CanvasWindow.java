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
    private int[] btn;

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
            drawTowerInfo();
            drawTowerUpgradeButton();
        }
    }

    public void drawTowerInfo() {
        String[] info = new String[]{"Id:", "Dmg:", "Range:", "Level:"};
        String[] value = new String[]{Integer.toString(tower.getId()), Integer.toString(this.tower.getValue()), Double.toString(this.tower.getRange()), Integer.toString(this.tower.getLevel())};
        gc.setFont(Font.font("Calibri", FontWeight.BOLD, this.text_size));
        gc.setFill(Paint.valueOf("#2aa32e"));
        for (int i = 0; i < info.length; i++) {
            gc.fillText(info[i], this.x + (double) this.text_size / 2, this.y + this.text_size * (i + 1));
            gc.fillText(value[i], this.x + this.text_size * 4, this.y + this.text_size * (i + 1));
        }
    }

    public void drawTowerUpgradeButton() {
        gc.setFill(Color.GREEN);
        gc.fillRect(this.btn[0], this.btn[1], this.btn[2], this.btn[3]);
    }

    public boolean isClickOnWindow(int x, int y) {
        if (x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.h) {
            if (x > this.btn[0] && x < this.btn[0] + this.btn[2] && y > this.btn[1] && y < this.btn[1] + this.btn[3]) {
                this.tower.lvlUp();
            }
            return true;
        } else {
            return false;
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
        this.btn = new int[]{this.x + this.w / 2 - this.text_size / 2, this.y + this.h - this.text_size, this.text_size, this.text_size};
    }

}
