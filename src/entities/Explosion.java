package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import map.Map;

public class Explosion extends Entity {
    private double radius;
    private int time;
    private double opacity;
    public Explosion(double pixelX, double pixelY, Color color, double radius) {
        super(pixelX, pixelY, color, 0);
        this.time = 20;
        this.radius = radius;
        this.opacity = 0.5;
    }

    @Override
    public void tick(Map map) {
        this.time--;
        this.opacity-=0.5/20;
    }

    @Override
    public void render(GraphicsContext g) {
        g.setFill(new Color(color.getRed(), color.getGreen(), color.getGreen(), opacity));
        g.fillOval(pixelX - radius, pixelY - radius, radius * 2, radius * 2);
    }

    public int getTime() {
        return time;
    }
}
