package sample;

import javafx.scene.paint.Color;

public enum Towers {

    LASER(0, 50, 5, 100, 5, Color.DARKRED, 100),
    KAHUR(1, 200, 20, 200, 3, Color.CYAN, 50),
    KUULIPILDUJA(2, 100, 10, 150, 5, Color.DARKOLIVEGREEN, 75),
    KÜLMUTAJA(3, 25, 0, 300, 10, Color.DEEPPINK, 50),
    MÜÜR(4, 500, 0, 100, 10, Color.GOLD, 0);

    private final int id;
    private final int hp;
    private final int dmg; //dmg, mis tower teeb ühes sekundis
    private final int hind;
    private final int maxLevel;
    private final Color color;
    private final double range;

    Towers(int id, int hp, int dmg, int hind, int maxLevel, Color color, int range) {
        this.id = id;
        this.hp = hp;
        this.dmg = dmg;
        this.hind = hind;
        this.maxLevel = maxLevel;
        this.color = color;
        this.range = range;
    }

    public int getId() {
        return id;
    }

    public int getHp() {
        return hp;
    }

    public int getDmg() {
        return dmg;
    }

    public int getHind() {
        return hind;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public Color getColor() {
        return color;
    }

    public double getRange() {
        return range;
    }

}
