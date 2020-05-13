package entities;

import blocks.towers.Tower;
import javafx.scene.canvas.GraphicsContext;
import map.Map;
import states.GameState;
import towerdefense.Main;

public class Projectile extends Entity {

    private final int damage;
    private final double diameter;
    private final Tower towerOfOrigin;
    private Monster target;
    private double direction;

    public Projectile(Tower towerOfOrigin, Monster target, int speed, double diameter) {
        super(towerOfOrigin.pixelX, towerOfOrigin.pixelY, towerOfOrigin.getColor(), speed);
        this.damage = towerOfOrigin.getDamage();
        this.diameter = diameter;
        this.target = target;
        this.towerOfOrigin = towerOfOrigin;
    }

    @Override
    public void tick(Map map) {
        Monster closest = getClosestMonster(map.getAllMonsters());
        double xDist = closest.getPixelX()-pixelX;
        double yDist = closest.getPixelY()-pixelY;
        double distance = Math.hypot(xDist, yDist);
        if (this.diameter*1.5>=distance){
            hitMonster(closest);
            target =null;
        }
        if (target != null) {
            xDist = target.getPixelX() - pixelX;
            yDist = target.getPixelY() - pixelY;
            distance = Math.hypot(xDist, yDist);
            direction = Math.atan2(yDist, xDist);
            if (this.speed < distance) {
                this.pixelX = this.pixelX + (speed * Math.cos(direction));
                this.pixelY = this.pixelY + (speed * Math.sin(direction));
            } else {
                this.pixelX = target.getPixelX();
                this.pixelY = target.getPixelY();
            }
            if (pixelX == target.getPixelX() && pixelY == target.getPixelY()) {
                hitMonster(target);
            } else if (target.isReachedNexus() || target.getHp() <= 0) {
                target =null; //getClosestMonster(map.getAllMonsters());
            }
        } else {
            if (pixelX < 0 || pixelX > Main.screenW || pixelY < 0 || pixelY > Main.screenH) {
                towerOfOrigin.getShotProjectiles().remove(this);
            } else {
                this.pixelX = this.pixelX + (speed * Math.cos(direction));
                this.pixelY = this.pixelY + (speed * Math.sin(direction));
            }
        }
    }

    @Override
    public void render(GraphicsContext g) {
        g.setFill(this.color);
        g.fillOval(pixelX - diameter / 2, pixelY - diameter / 2, diameter, diameter);
    }
    private void hitMonster(Monster monster){
        if (towerOfOrigin.getId()==11){
            towerOfOrigin.getactiveExplosions().add(new Explosion(this.pixelX, this.pixelY, color.RED,towerOfOrigin.getExplosionRadius()));
            for (Monster monster1:GameState.map.getAllMonsters()){
                double xDist = monster1.getPixelX()-this.pixelX;
                double yDist = monster1.getPixelY()-this.pixelY;
                double distance = Math.hypot(xDist, yDist);
                if (distance<=towerOfOrigin.getExplosionRadius()){
                    monster1.setHp(monster1.getHp() - this.damage);
                }
            }
        }else{
            monster.setHp(monster.getHp() - this.damage);
        }
        towerOfOrigin.getShotProjectiles().remove(this);
    }
}
