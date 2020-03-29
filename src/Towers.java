import javafx.scene.paint.Color;

public enum Towers {

    LASER("Laser", 0, 5, 100, 5, Color.DARKRED, 100),//range peaks scalima mapi blocki suurusega (Main.getMap().getSize())
    KAHUR("Kahur", 1, 20, 200, 3, Color.CYAN, 50),
    KUULIPILDUJA("Kuulipilduja", 2, 10, 150, 5, Color.DARKOLIVEGREEN, 75),
    KÜLMUTAJA("Külmutaja", 3, 0, 300, 10, Color.DEEPPINK, 50),
    MÜÜR("Müür", 4, 0, 100, 10, Color.GOLD, 0);

    private final String nimi;
    private final int id;
    private final int dmg; //dmg, mis tower teeb ühes sekundis
    private final int hind;
    private final int maxLevel;
    private final Color color;
    private final double range;

    Towers(String nimi, int id, int dmg, int hind, int maxLevel, Color color, int range) {
        this.nimi = nimi;
        this.id = id;
        this.dmg = dmg;
        this.hind = hind;
        this.maxLevel = maxLevel;
        this.color = color;
        this.range = range;
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

}
