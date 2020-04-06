package blocks.towers;

import javafx.scene.paint.Color;
import towerdefense.Main;

public enum Towers {

    LASER("Laser", 0, 1, 100, 5, Color.DARKRED, Main.blockSize*6, 0),  //range peaks scalima mapi blocki suurusega (towerdefense.Main.getMap().getSize())
    KAHUR("Kahur", 1, 20, 200, 3, Color.CYAN, Main.blockSize*3, 60),
    KUULIPILDUJA("Kuulipilduja", 2, 15, 150, 5, Color.DARKOLIVEGREEN, Main.blockSize*5, 20),
    KÜLMUTAJA("Külmutaja", 3, 30, 300, 10, Color.DEEPPINK, Main.blockSize*3, 30),
    MÜÜR("Müür", 4, 0, 100, 10, Color.GOLD, 0, 0);

    private final String nimi;
    private final int id;
    private final int dmg; //dmg, mis tower teeb ühes sekundis
    private final int hind;
    private final int maxLevel;
    private final Color color;
    private final double range;
    private final int cooldown;

    Towers(String nimi, int id, int dmg, int hind, int maxLevel, Color color, int range, int cooldown) {
        this.nimi = nimi;
        this.id = id;
        this.dmg = dmg;
        this.hind = hind;
        this.maxLevel = maxLevel;
        this.color = color;
        this.range = range;
        this.cooldown = cooldown;
    }

    public String getNimi() {
        return nimi;
    }

    public int getId() {
        return id;
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

    public int getCooldown() {
        return cooldown;
    }
}
