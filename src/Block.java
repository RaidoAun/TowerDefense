import javafx.scene.paint.Color;

public class Block {

    public static Block SEIN(int x, int y) {
        return new Block(x, y, 1, 1, Color.BLACK, 0);
    }

    protected int x;
    protected int y;
    protected int id;
    protected Color color;
    protected int value;
    protected int pathCount;  //Näitab, mitu pathi hetkel sellest blokist läbi läheb.

    public Block(int x, int y, int block_id, int block_value, Color block_color, int pathCount) {
        //0 - vaba; 1 - sein; 3 - nexus; 2 - start; 10 - tower; 9 - path
        this.x = x;
        this.y = y;
        this.id = block_id;
        this.color = block_color;
        this.value = block_value;
        this.pathCount = pathCount;
    }

    public Block(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public void reconstruct(int id, int value, Color color, int pathCount) {
        this.id = id;
        this.value = value;
        this.color = color;
        this.pathCount = pathCount;
    }

    public void reconstruct(Blocks blokk) {
        this.id = blokk.getId();
        this.value = blokk.getId();
        this.color = blokk.getColor();
        this.pathCount = 0;
    }

    public int getPathCount() {
        return pathCount;
    }

    public void setPathCount(int pathCount) {
        this.pathCount = pathCount;
    }

    Color getColor() {
        return color;
    }

    void setColor(Color color) {
        this.color = color;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }

}
