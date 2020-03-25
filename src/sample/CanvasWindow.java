package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasWindow {
    private int x;
    private int y;
    private int w;
    private int h;
    private Canvas c;
    private boolean active;
    private Color color = new Color(1,1,1,0.8);
    CanvasWindow(Canvas c){
        this.c = c;
    }
    public void draw(){
        if (this.active){
            GraphicsContext gc =  c.getGraphicsContext2D();
            gc.setFill(color);
            gc.fillRect(x,y,w,h);
        }
    }
    public void set(int x,int y,int w,int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getH() {
        return h;
    }

    public Canvas getC() {
        return c;
    }

    public int getW() {
        return w;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
