import javafx.scene.paint.Color;

public enum Blocks {

    SEIN("Sein", 1, Color.BLACK),
    VABA("Vaba", 0, Color.WHITE),
    SPAWNPOINT("Spawnpoint", 2, Color.LIGHTGREEN),
    NEXUS("Nexus", 3, Color.VIOLET);

    private final String nimi;
    private final int id;
    private final Color color;

    Blocks(String nimi, int id, Color color) {
        this.nimi = nimi;
        this.id = id;
        this.color = color;
    }

    public String getNimi() {
        return nimi;
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }
}
