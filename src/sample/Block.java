package sample;

import javafx.scene.paint.Color;

public class Block {
    private int id;
    private Color color;
    private int value;
    public Block(int block_id,int block_value,Color block_color){
        id = block_id;
        color = block_color;
        value = block_value;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
