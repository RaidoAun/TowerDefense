package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import map.Map;
import blocks.towers.Tower;

public class Projectile extends Entity {

    private final int damage;
    private final double diameter;
    private final Tower towerOfOrigin;
    private Monster target;

    public Projectile(int startX, int startY, int damage, int speed, double diameter, Color color, Monster target, Tower towerOfOrigin) {
        super(startX, startY, color, speed);
        this.damage = damage;
        this.diameter = diameter;
        this.target = target;
        this.towerOfOrigin = towerOfOrigin;
    }

    @Override
    public void tick(Map map) {
        double xDist = target.getPixelX() - pixelX;
        double yDist = target.getPixelY() - pixelY;
        double distance = Math.hypot(xDist, yDist);
        if (this.speed <= distance) {
            this.pixelX += (xDist * this.speed) / distance;
            this.pixelY += (yDist * this.speed) / distance;
        } else {
            this.pixelX = target.getPixelX();
            this.pixelY = target.getPixelY();
        }
        if (pixelX == target.getPixelX() && pixelY == target.getPixelY()) {
            target.setHp(target.getHp() - damage);
            towerOfOrigin.getShotProjectiles().remove(this);
        } else if (target.isReachedNexus()) {
            towerOfOrigin.getShotProjectiles().remove(this);
        } else if (target.getHp() <= 0) {
            towerOfOrigin.getShotProjectiles().remove(this);
        }
    }

    @Override
    public void render(GraphicsContext g) {
       g.setFill(this.color);
       g.fillOval(pixelX - diameter / 2, pixelY - diameter / 2, diameter, diameter);
    }
}
