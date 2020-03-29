import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Tower extends Block {

    private double x;
    private double y;
    private int level;
    private double range;
    private boolean active;
    private int damage;
    private int hind;
    private int maxLevel;
    private List<Projectile> shotMissles;

    public Tower(Towers type, double x, double y) {
        super(type.getId() + 10, type.getDmg(), type.getColor(), 0);
        this.x = x;
        this.y = y;
        this.level = 1;
        this.range = type.getRange();
        this.active = false;
        this.damage = this.value;
        this.hind = type.getHind();
        this.maxLevel = type.getMaxLevel();
        this.shotMissles = new ArrayList<>();
    }

    public Block getBlock() {
        return new Block(this.id, this.value, this.color, this.getPathCount());
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

    public double getPixelX() {
        return x;
    }

    public double getPixelY() {
        return y;
    }

    public double getRange() {
        return range;
    }

    void shoot(List<Monster> monsters, boolean onlyAnimate) {
        for (Monster monster : monsters) {
            int dist = (int) Math.round(Math.sqrt(Math.pow(this.x - monster.getX(), 2) + Math.pow(this.y - monster.getY(), 2)));
            if (dist <= this.range && this.id == 11) {
                if (!onlyAnimate) { //Tähendab, et tuleb tulistada (uus projectile luua).
                    Projectile missle = new Projectile(this.x, this.y, this.damage, 0.25, 10, this.color);
                    shotMissles.add(missle);
                    monster.addMissle(missle);
                }
            } else if (dist <= this.range && this.id == 10) {
                if (!onlyAnimate) monster.setHp(monster.getHp() - this.damage);
                Main.getGc().setStroke(getColor());
                Main.getGc().setLineWidth(0.5);
                Main.getGc().strokeLine(this.x, this.y, monster.getX(), monster.getY());
            }
            monster.updateMisslesEndpoint();
            monster.pullMissles();
        }
    }

    void drawRange() {

        Main.getGc().setFill(new Color(0, 0, 0, 0.5));
        Main.getGc().fillOval(this.x - this.range, this.y - this.range, this.range * 2, this.range * 2);

    }

    public int getDamage() {
        return damage;
    }

}
