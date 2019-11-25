package sample;

import javafx.scene.paint.Color;

public class Block {
    private int id;
    private Color color;
    private int value;
    private int level;
    public Block(int block_id,int block_value,Color block_color){
        setId(block_id);
        setColor(block_color);
        setValue(block_value);
        setLevel(0);
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
