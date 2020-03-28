import javafx.scene.paint.Color;

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

    public Tower(Towers type, double x, double y) {
        super(type.getId() + 10, type.getDmg(), type.getColor());
        this.x = x;
        this.y = y;
        this.level = 1;
        this.range = type.getRange();
        this.active = false;
        this.damage = this.value;
        this.hind = type.getHind();
        this.maxLevel = type.getMaxLevel();
    }

    public Block getBlock() {
        return new Block(this.id, this.value, this.color);
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

    void shoot(List<Monster> monsters) {

        for (Monster monster : monsters) {
            int dist = (int) Math.round(Math.sqrt(Math.pow(this.x - monster.getX(), 2) + Math.pow(this.y - monster.getY(), 2)));
            if (dist <= this.range) {
                monster.setHp(monster.getHp() - this.damage);
                Main.getGc().setStroke(getColor());
                Main.getGc().setLineWidth(0.5);
                Main.getGc().strokeLine(this.x, this.y, monster.getX(), monster.getY());
            }
        }
    }

    void drawRange() {

        Main.getGc().setFill(new Color(0, 0, 0, 0.5));
        Main.getGc().fillOval(this.x - this.range, this.y - this.range, this.range * 2, this.range * 2);

    }

}
