package blocks;

import javafx.scene.paint.Color;
import tools.Converter;

public class Block {
    public int indexX;
    public int indexY;
    public double pixelX;
    public double pixelY;
    public int id;
    protected Color color;
    protected int value;
    private int pathCount;  //Näitab, mitu pathi hetkel sellest blokist läbi läheb.

    public Block(int indexX, int indexY, int id, int value, Color color, int pathCount) {
        //0 - vaba; 1 - sein; 3 - nexus; 2 - start; 10 - tower; 9 - path
        this.id = id;
        this.color = color;
        this.value = value;
        this.pathCount = pathCount;
        this.indexX = indexX;
        this.indexY = indexY;
        this.pixelX = Converter.indexToPix(indexX);
        this.pixelY = Converter.indexToPix(indexY);
    }

    public int getPathCount() {
        return pathCount;
    }

    public void setPathCount(int pathCount) {
        this.pathCount = pathCount;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setWall() {
        this.id = 1;
        this.pathCount = 0;
        this.value = 1;
        this.color = Color.BLACK;
    }

}
