package entities;

import blocks.towers.Tower;
import javafx.scene.canvas.GraphicsContext;
import map.Map;

public class Projectile extends Entity {

    private final int damage;
    private final double diameter;
    private final Tower towerOfOrigin;
    private Monster target;

    public Projectile(Tower towerOfOrigin, Monster target, int speed, double diameter) {
        super(towerOfOrigin.pixelX, towerOfOrigin.pixelY, towerOfOrigin.getColor(), speed);
        this.damage = towerOfOrigin.getDamage();
        this.diameter = diameter;
        this.target = target;
        this.towerOfOrigin = towerOfOrigin;
    }

    @Override
    public void tick(Map map) {
        double xDist = target.getPixelX() - pixelX;
        double yDist = target.getPixelY() - pixelY;
        double direction = Math.atan2(yDist, xDist);
        double distance = Math.hypot(xDist, yDist);
        if (this.speed < distance) {
            this.pixelX = this.pixelX + (speed * Math.cos(direction));
            this.pixelY = this.pixelY + (speed * Math.sin(direction));
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
