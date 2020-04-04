import javafx.scene.paint.Color;

import java.util.List;

public class Tower extends Block {

    private int level;
    private double range;
    private boolean active;
    private int damage;
    private int hind;
    private int maxLevel;

    public Tower(Towers type, int x, int y, int size) {
        super(x, y, type.getId() + 10, type.getDmg(), type.getColor(), 0, size);
        this.level = 1;
        this.range = type.getRange();
        this.active = false;
        this.damage = this.value;
        this.hind = type.getHind();
        this.maxLevel = type.getMaxLevel();
    }

    void lvlUp() {
        this.level += 1;
        this.range += 10;
        this.damage += 1;
    }

    public int getLevel() {
        return level;
    }

    boolean isActive() {
        return active;
    }

    void setActive(boolean active) {
        this.active = active;
    }

    public int getPixelX() {
        return x;
    }

    public int getPixelY() {
        return y;
    }

    public double getRange() {
        return range;
    }

    public Monster getClosestMonster(List<Monster> monsters) {
        Monster closestMonster = monsters.get(0);
        double closestDistance = Math.hypot(this.x - closestMonster.getX(), this.y - closestMonster.getY());
        for (Monster monster : monsters) {
            double distance = Math.hypot(this.x - monster.getX(), this.y - monster.getY());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestMonster = monster;
            }
        }
        if (closestDistance <= this.range) {
            return closestMonster;
        } else {
            return null;
        }
    }

    public void cannonNewMissile(Monster monster) {
        Projectile missile = new Projectile(this.x, this.y, this.damage, 500, 10, this.color);
        monster.addMissile(missile);
    }

    public int getDamage() {
        return damage;
    }

    public int getHind() {
        return hind;
    }
}
