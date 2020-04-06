package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import map.Map;

public abstract class Entity {

    protected int pixelY;
    protected int pixelX;
    protected Color color;
    protected int speed; //Mitu pikslit liigub ühe framega.

    public Entity(int pixelX, int pixelY, Color color, int speed) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        this.color = color;
        this.speed = speed;
    }

    public abstract void tick(Map map);

    public abstract void render(GraphicsContext g);

    public int getPixelX() {
        return pixelX;
    }

    public int getPixelY() {
        return pixelY;
    }

    public int distanceFrom(int pixelX, int pixelY) {
        return (int) Math.hypot(pixelX - this.pixelX, pixelY - this.pixelY);
    }

    public Color getColor() {
        return color;
    }
}
