import javafx.scene.paint.Color;

public class Block {
    int id;
    Color color;
    int value;
    private int pathCount;  //Näitab, mitu pathi hetkel sellest blokist läbi läheb.

    public Block(int block_id, int block_value, Color block_color, int pathCount) {
        //0 - vaba; 1 - sein; 3 - nexus; 2 - start; 10 - tower; 9 - path
        this.id = block_id;
        this.color = block_color;
        this.value = block_value;
        this.pathCount = pathCount;
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
