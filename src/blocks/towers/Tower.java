package blocks.towers;

import blocks.Block;
import entities.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import entities.Monster;
import entities.Projectile;
import tools.Converter;
import map.Map;
import towerdefense.Main;

import java.util.HashSet;
import java.util.List;

public abstract class Tower extends Block {

    protected HashSet<Projectile> shotProjectiles;
    protected int level;
    protected double range;
    protected boolean active;
    protected int damage;
    protected int hind;
    protected int maxLevel;
    protected int cooldown; //Mitme frame tagant teeb damage (tulistab).
    protected int idleTime; //Kaua tower pole tulistanud (framed).

    public Tower(Towers type, int x, int y) {
        super(x, y, type.getId() + 10, type.getDmg(), type.getColor(), 0);
        this.level = 1;
        this.range = type.getRange();
        this.active = false;
        this.damage = type.getDmg();
        this.hind = type.getHind();
        this.maxLevel = type.getMaxLevel();
        this.cooldown = type.getCooldown();
        this.idleTime = cooldown;
        this.shotProjectiles = new HashSet<>();
    }

    public Block getBlock() {
        return new Block(this.indexX, this.indexY, this.id, this.value, this.color, this.getPathCount());
    }

    public abstract void tick(Map map);

    public abstract void render(GraphicsContext g);

    public void lvlUp() {
        this.level += 1;
        this.range += 10;
        this.damage += 1;
    }

    public int getLevel() {
        return level;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPixelX() {
        return Converter.indexToPix(indexX);
    }

    public int getPixelY() {
        return Converter.indexToPix(indexY);
    }

    public double getRange() {
        return range;
    }

    public Monster getClosestMonster(HashSet<Monster> monsters) {

        Monster closestMonster = null;
        double closestDistance = Double.POSITIVE_INFINITY;

        for (Monster monster : monsters) {
            double distance = monster.distanceFrom(this.pixelX, this.pixelY);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestMonster = monster;
            }
        }
        return closestMonster;
    }

    public void drawRange(GraphicsContext g) {

        g.setFill(new Color(0, 0, 0, 0.5));
        g.fillOval(Converter.indexToPix(this.indexX) - this.range, Converter.indexToPix(this.indexY) - this.range, this.range * 2, this.range * 2);

    }

    public int getDamage() {
        return damage;
    }

    public int getHind() {
        return hind;
    }

    public HashSet<Projectile> getShotProjectiles() {
        return shotProjectiles;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
