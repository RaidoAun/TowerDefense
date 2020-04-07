package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import map.Map;

public abstract class Entity {

    protected double pixelY;
    protected double pixelX;
    protected Color color;
    protected double speed; //Mitu pikslit liigub ühe framega.

    public Entity(double pixelX, double pixelY, Color color, double speed) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        this.color = color;
        this.speed = speed;
    }

    public abstract void tick(Map map);

    public abstract void render(GraphicsContext g);

    public double getPixelX() {
        return pixelX;
    }

    public double getPixelY() {
        return pixelY;
    }

    public double distanceFrom(double pixelX, double pixelY) {
        return Math.hypot(pixelX - this.pixelX, pixelY - this.pixelY);
    }

    public Color getColor() {
        return color;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
