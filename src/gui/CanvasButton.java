package gui;

import javafx.scene.paint.Color;

public class CanvasButton {
    private Color color;
    private Runnable func;
    private int x;
    private int y;
    private int w;
    private int h;

    public CanvasButton(Runnable func) {
        this.func = func;

    }

    public void onPressed() {
        this.func.run();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void setCoords(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
